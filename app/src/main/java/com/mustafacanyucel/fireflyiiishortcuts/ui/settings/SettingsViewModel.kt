package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
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
    private val authManager: Oauth2Manager
) : ViewModelBase() {

    private val _serverUrl = MutableStateFlow(STRING_NOT_SET_VALUE)
    private val _clientId = MutableStateFlow(STRING_NOT_SET_VALUE)
    private val _registeredRedirectUrl =
        MutableStateFlow("https://fireflyiiishortcuts.mustafacanyucel.com/oauth2redirect")
    private val _isBusy = MutableStateFlow(false)
    private val _statusText = MutableStateFlow("Idle...")
    private val _syncProgress = MutableStateFlow(0)


    val serverUrl = _serverUrl.asStateFlow()
    val clientId = _clientId.asStateFlow()
    val isBusy = _isBusy.asStateFlow()
    val statusText = _statusText.asStateFlow()
    val registeredRedirectUrl = _registeredRedirectUrl.asStateFlow()
    val syncProgress = _syncProgress.asStateFlow()
    val maxProgress = 3
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

    companion object {
        private const val STRING_NOT_SET_VALUE = "Not set"
    }

}