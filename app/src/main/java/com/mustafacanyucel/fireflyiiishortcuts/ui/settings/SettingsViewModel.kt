package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: IPreferencesRepository
) : ViewModel() {

    private val _serverUrl = MutableStateFlow("aaaaaa")


    val serverUrl = _serverUrl.asStateFlow()


    init {
        viewModelScope.launch {
            val savedUrl = preferencesRepository.getString(SERVER_URL_KEY, SERVER_NOT_SET_VALUE)
            _serverUrl.value = savedUrl
        }
    }

    fun saveServerUrl() {

    }

    fun setServerUrl(url: String) {
        _serverUrl.value = url
    }

    companion object {
        private const val SERVER_URL_KEY = "server_url"
        private const val SERVER_NOT_SET_VALUE = "Not set"
    }

}