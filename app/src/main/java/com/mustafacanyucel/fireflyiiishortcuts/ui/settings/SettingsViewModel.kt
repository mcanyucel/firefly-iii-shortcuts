package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: IPreferencesRepository,
) : ViewModelBase() {

    private val _serverUrl = MutableStateFlow(SERVER_NOT_SET_VALUE)

    val serverUrl = _serverUrl.asStateFlow()

    init {
        viewModelScope.launch {
            val savedUrl = preferencesRepository.getString(SERVER_URL_KEY, SERVER_NOT_SET_VALUE)
            _serverUrl.value = savedUrl
        }
    }

    fun saveServerUrl() {
        if (validateServerUrl(_serverUrl.value)) {

            viewModelScope.launch {
                try {
                    preferencesRepository.saveString(SERVER_URL_KEY, _serverUrl.value)
                    emitEvent(EventType.SUCCESS, "Server URL saved.")
                } catch (e: Exception) {
                    emitEvent(EventType.ERROR, e.message ?: "Error")
                }
            }
        } else {
            emitEvent(EventType.ERROR, "Invalid server url!")
        }
    }

    fun setServerUrl(url: String) {
        _serverUrl.value = url
    }

    private fun validateServerUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches() &&
                (url.startsWith("http://") || url.startsWith("https://"))
    }

    companion object {
        private const val SERVER_URL_KEY = "server_url"
        private const val SERVER_NOT_SET_VALUE = "Not set"
    }

}