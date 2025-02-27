package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import com.mustafacanyucel.fireflyiiishortcuts.model.api.AccountData
import com.mustafacanyucel.fireflyiiishortcuts.services.auth.Oauth2Manager
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.ApiResult
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.IAccountRepository
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
    private val accountRepository: IAccountRepository
) : ViewModelBase() {

    private val _serverUrl = MutableStateFlow(STRING_NOT_SET_VALUE)
    private val _clientId = MutableStateFlow(STRING_NOT_SET_VALUE)
    private val _registeredRedirectUrl =
        MutableStateFlow("https://fireflyiiishortcuts.mustafacanyucel.com/oauth2redirect.html")
    private val _isBusy = MutableStateFlow(false)
    private val _statusText = MutableStateFlow("Idle...")
    private val _syncProgress = MutableStateFlow(0)
    private val _accounts = MutableStateFlow<List<AccountData>>(emptyList())
    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState = _uiState.asStateFlow()


    val serverUrl = _serverUrl.asStateFlow()
    val clientId = _clientId.asStateFlow()
    val isBusy = _isBusy.asStateFlow()
    val statusText = _statusText.asStateFlow()
    val registeredRedirectUrl = _registeredRedirectUrl.asStateFlow()
    val syncProgress = _syncProgress.asStateFlow()
    val maxProgress = 3
    val accounts = _accounts.asStateFlow()
    val isAuthorized = authManager.authState.map { state ->
        if (state?.isAuthorized == true)
            "Authorized"
        else
            "Not Authorized"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Not Authorized"
    )


    init {
        viewModelScope.launch {
            val savedUrl = preferencesRepository.getString(
                preferencesRepository.serverUrlKey,
                STRING_NOT_SET_VALUE
            )
            _serverUrl.value = savedUrl
            val savedClientId = preferencesRepository.getString(
                preferencesRepository.clientIdKey,
                STRING_NOT_SET_VALUE
            )
            _clientId.value = savedClientId
        }
    }

    fun saveServerSettings() {
        if (validateServerInputs()) {
            viewModelScope.launch {
                try {
                    preferencesRepository.saveString(
                        preferencesRepository.serverUrlKey,
                        _serverUrl.value
                    )
                    preferencesRepository.saveString(
                        preferencesRepository.clientIdKey,
                        _clientId.value
                    )
                    preferencesRepository.saveString(
                        preferencesRepository.registeredRedirectUrl,
                        _registeredRedirectUrl.value
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
        return Patterns.WEB_URL.matcher(url).matches() &&
                (url.startsWith("http://") || url.startsWith("https://"))
    }

    private fun validateClientId(clientId: String): Boolean {
        return clientId.isNotEmpty() && clientId != STRING_NOT_SET_VALUE
    }

    private fun validateRegisteredReturnUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches() &&
                (url.startsWith("http://") || url.startsWith("https://"))
    }

    private fun validateServerInputs(): Boolean {
        return validateServerUrl(_serverUrl.value)
                && validateClientId(_clientId.value)
                && validateRegisteredReturnUrl(_registeredRedirectUrl.value)
    }

    fun loadAccounts() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                Log.d("AccountsViewModel", "Starting to load accounts")
                accountRepository.getAccounts()
                    .collect { result ->
                        when (result) {
                            is ApiResult.Success -> {
                                Log.d("AccountsViewModel", "Successfully loaded ${result.data.size} accounts")
                                _uiState.value = _uiState.value.copy(
                                    accounts = result.data,
                                    isLoading = false,
                                    lastLoadTime = System.currentTimeMillis()
                                )
                                emitEvent(EventType.SUCCESS, "Loaded ${result.data.size} accounts")
                            }
                            is ApiResult.Error -> {
                                Log.e("AccountsViewModel", "Error loading accounts: ${result.message}")
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                                emitEvent(EventType.ERROR, result.message)
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e("AccountsViewModel", "Unexpected error in loadAccounts", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unexpected error: ${e.message}"
                )
                emitEvent(EventType.ERROR, "Unexpected error: ${e.message}")
            }
        }
    }

    companion object {
        private const val STRING_NOT_SET_VALUE = "Not set"
    }

}

data class AccountsUiState(
    val accounts: List<AccountData> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val lastLoadTime: Long = 0
)