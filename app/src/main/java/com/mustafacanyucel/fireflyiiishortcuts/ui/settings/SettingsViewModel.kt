package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.ApiResult
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import com.mustafacanyucel.fireflyiiishortcuts.model.api.account.AccountData
import com.mustafacanyucel.fireflyiiishortcuts.model.api.category.CategoryData
import com.mustafacanyucel.fireflyiiishortcuts.services.auth.Oauth2Manager
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: IPreferencesRepository,
    private val authManager: Oauth2Manager,
    private val remoteFireflyRepository: RemoteFireflyRepository,
    private val localFireflyRepository: LocalFireflyRepository
) : ViewModelBase() {

    private val _serverUrl = MutableStateFlow(STRING_NOT_SET_VALUE)
    private val _clientId = MutableStateFlow(STRING_NOT_SET_VALUE)
    private val _registeredRedirectUrl =
        MutableStateFlow("https://fireflyiiishortcuts.mustafacanyucel.com/oauth2redirect.html")
    private val _isBusy = MutableStateFlow(false)
    private val _statusText = MutableStateFlow("Idle...")
    private val _syncProgress = MutableStateFlow(0)
    private val _accounts = MutableStateFlow<List<AccountData>>(emptyList())
    private val _categories = MutableStateFlow<List<CategoryData>>(emptyList())
    private var _syncedAccounts = 0
    private var _syncedBudgets = 0
    private var _syncedCategories = 0
    private var _syncedTags = 0
    private var _syncedPiggybanks = 0
    private var _syncedBills = 0

    val serverUrl = _serverUrl.asStateFlow()
    val clientId = _clientId.asStateFlow()
    val isBusy = _isBusy.asStateFlow()
    val statusText = _statusText.asStateFlow()
    val registeredRedirectUrl = _registeredRedirectUrl.asStateFlow()
    val syncProgress = _syncProgress.asStateFlow()
    val maxProgress = 6
    val accounts = _accounts.asStateFlow()
    val isAuthorized = authManager.authState.map { state ->
        if (state?.isAuthorized == true) "Authorized"
        else "Not Authorized"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Not Authorized"
    )

    init {
        viewModelScope.launch {
            val savedUrl = preferencesRepository.getString(
                preferencesRepository.serverUrlKey, STRING_NOT_SET_VALUE
            )
            _serverUrl.value = savedUrl
            val savedClientId = preferencesRepository.getString(
                preferencesRepository.clientIdKey, STRING_NOT_SET_VALUE
            )
            _clientId.value = savedClientId
        }
    }

    fun saveServerSettings() {
        if (validateServerInputs()) {
            viewModelScope.launch {
                try {
                    preferencesRepository.saveString(
                        preferencesRepository.serverUrlKey, _serverUrl.value
                    )
                    preferencesRepository.saveString(
                        preferencesRepository.clientIdKey, _clientId.value
                    )
                    preferencesRepository.saveString(
                        preferencesRepository.registeredRedirectUrl, _registeredRedirectUrl.value
                    )
                    emitEvent(EventType.SUCCESS, "Server settings saved.")
                } catch (e: Exception) {
                    emitEvent(EventType.ERROR, e.message ?: "Error")
                }
            }
        } else {
            emitEvent(EventType.ERROR, "Invalid server settings!")
        }
    }

    fun setServerUrl(url: String) {
        _serverUrl.value = url
    }

    fun setRegisteredRedirectUrl(url: String) {
        _registeredRedirectUrl.value = url
    }

    fun startAuthentication(activity: Activity) {
        authManager.startAuthorizationFlow(activity, Oauth2Manager.RC_AUTH)
    }

    fun logout() {
        authManager.logout()
    }

    fun syncServerData() {
        if (isBusy.value) return

        viewModelScope.launch {
            _syncProgress.value = 0
            _isBusy.value = true
            _statusText.value = "Syncing account data..."
            delay(2000)
            _syncProgress.value++
            _statusText.value = "Syncing categories..."
            delay(2000)
            _syncProgress.value++
            _statusText.value = "Syncing budgets..."
            delay(2000)
            _syncProgress.value++
            _statusText.value = "Sync completed."
            _isBusy.value = false
        }
    }

    fun setClientId(clientId: String) {
        _clientId.value = clientId
    }

    private fun validateServerUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url)
            .matches() && (url.startsWith("http://") || url.startsWith("https://"))
    }

    private fun validateClientId(clientId: String): Boolean {
        return clientId.isNotEmpty() && clientId != STRING_NOT_SET_VALUE
    }

    private fun validateRegisteredReturnUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url)
            .matches() && (url.startsWith("http://") || url.startsWith("https://"))
    }

    private fun validateServerInputs(): Boolean {
        return validateServerUrl(_serverUrl.value) && validateClientId(_clientId.value) && validateRegisteredReturnUrl(
            _registeredRedirectUrl.value
        )
    }

    fun syncData() {
        viewModelScope.launch {
            _isBusy.value = true
            _syncProgress.value = 0
            _statusText.value = "Syncing accounts..."
            syncAccounts()
            _syncProgress.value = 1
            _statusText.value = "Syncing categories..."
            syncCategories()
            _syncProgress.value = 2
            _statusText.value = "Syncing budgets..."
            syncBudgets()
            _syncProgress.value = 3
            _statusText.value = "Syncing tags..."
            syncTags()
            _syncProgress.value = 4
            _statusText.value = "Syncing piggybanks..."
            syncPiggybanks()
            _syncProgress.value = 5
            _statusText.value = "Syncing bills..."
            syncBills()
            _syncProgress.value = 6
            _statusText.value =
                "Synced $_syncedAccounts accounts, $_syncedCategories categories, " +
                        "$_syncedBudgets budgets, $_syncedTags tags, " +
                        "$_syncedBills bills, and $_syncedPiggybanks piggybanks."
            _isBusy.value = false
        }
    }

    private suspend fun syncCategories() {
        try {
            _syncedCategories = 0
            remoteFireflyRepository.getCategories().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d(
                            "SettingsViewModel",
                            "Successfully loaded ${result.data.size} categories"
                        )
                        localFireflyRepository.saveCategories(result.data)
                        _syncedCategories = result.data.size
                    }

                    is ApiResult.Error -> {
                        Log.e(
                            "SettingsViewModel", "Error loading categories: ${result.message}"
                        )
                        emitEvent(EventType.ERROR, result.message)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Unexpected error in syncCategories", e)
            emitEvent(EventType.ERROR, "Unexpected error: ${e.message}")
        }
    }

    private suspend fun syncPiggybanks() {
        try {
            _syncedPiggybanks = 0
            remoteFireflyRepository.getPiggybanks().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d(
                            "SettingsViewModel",
                            "Successfully loaded ${result.data.size} piggybanks"
                        )
                        localFireflyRepository.savePiggybanks(result.data)
                        _syncedPiggybanks = result.data.size
                    }

                    is ApiResult.Error -> {
                        Log.e(
                            "SettingsViewModel", "Error loading piggybanks: ${result.message}"
                        )
                        emitEvent(EventType.ERROR, result.message)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Unexpected error in syncPiggybanks", e)
            emitEvent(EventType.ERROR, "Unexpected error: ${e.message}")
        }
    }

    private suspend fun syncBills() {
        try {
            _syncedBills = 0
            remoteFireflyRepository.getBills().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d(
                            "SettingsViewModel", "Successfully loaded ${result.data.size} bills"
                        )
                        localFireflyRepository.saveBills(result.data)
                        _syncedBills = result.data.size
                    }

                    is ApiResult.Error -> {
                        Log.e(
                            "SettingsViewModel", "Error loading bills: ${result.message}"
                        )
                        emitEvent(EventType.ERROR, result.message)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Unexpected error in syncBills", e)
            emitEvent(EventType.ERROR, "Unexpected error: ${e.message}")
        }
    }

    private suspend fun syncBudgets() {
        try {
            _syncedBudgets = 0
            remoteFireflyRepository.getBudgets().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d(
                            "SettingsViewModel", "Successfully loaded ${result.data.size} budgets"
                        )
                        localFireflyRepository.saveBudgets(result.data)
                        _syncedBudgets = result.data.size
                    }

                    is ApiResult.Error -> {
                        Log.e(
                            "SettingsViewModel", "Error loading budgets: ${result.message}"
                        )
                        emitEvent(EventType.ERROR, result.message)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Unexpected error in syncBudgets", e)
            emitEvent(EventType.ERROR, "Unexpected error: ${e.message}")
        }
    }

    private suspend fun syncTags() {
        try {
            _syncedTags = 0
            remoteFireflyRepository.getTags().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d(
                            "SettingsViewModel", "Successfully loaded ${result.data.size} tags"
                        )
                        localFireflyRepository.saveTags(result.data)
                        _syncedTags = result.data.size
                    }

                    is ApiResult.Error -> {
                        Log.e(
                            "SettingsViewModel", "Error loading tags: ${result.message}"
                        )
                        emitEvent(EventType.ERROR, result.message)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Unexpected error in syncTags", e)
            emitEvent(EventType.ERROR, "Unexpected error: ${e.message}")
        }
    }

    private suspend fun syncAccounts() {
        try {
            _syncedAccounts = 0
            remoteFireflyRepository.getAccounts().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d(
                            "SettingsViewModel", "Successfully loaded ${result.data.size} accounts"
                        )
                        localFireflyRepository.saveAccounts(result.data)
                        _syncedAccounts = result.data.size
                    }

                    is ApiResult.Error -> {
                        Log.e(
                            "SettingsViewModel", "Error loading accounts: ${result.message}"
                        )
                        emitEvent(EventType.ERROR, result.message)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Unexpected error in syncAccounts", e)
            emitEvent(EventType.ERROR, "Unexpected error: ${e.message}")
        }
    }

    companion object {
        private const val STRING_NOT_SET_VALUE = "Not set"
    }
}