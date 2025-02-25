package com.mustafacanyucel.fireflyiiishortcuts.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    public val message:String = "hello from viewmodel!"
}