package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import androidx.lifecycle.ViewModel
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: IPreferencesRepository
) : ViewModel() {
    public var message:String = preferencesRepository.getStringFlow("test", "this value does not exist!").toString()


}