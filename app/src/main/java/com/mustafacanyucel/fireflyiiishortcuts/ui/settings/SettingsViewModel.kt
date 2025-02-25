package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.services.dialog.IDialogService
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
        Log.d("DEBUG", "Setting ${_serverUrl.value}")
        if (_serverUrl.value.equals(SERVER_NOT_SET_VALUE, true)) {
            Log.d("DEBUG", "Values are equal")
            emitError("Invalid server url!")
        }
    }

    fun setServerUrl(url: String) {
        _serverUrl.value = url
    }

    companion object {
        private const val SERVER_URL_KEY = "server_url"
        private const val SERVER_NOT_SET_VALUE = "Not set"
    }

}