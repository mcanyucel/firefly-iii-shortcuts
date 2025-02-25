package com.mustafacanyucel.fireflyiiishortcuts.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.model.ErrorEventData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class ViewModelBase : ViewModel() {
    private val _errorEvent = MutableSharedFlow<ErrorEventData>()
    val errorEvent = _errorEvent.asSharedFlow()

    protected fun emitError(message: String, action: (()->Unit)? = null) {
        viewModelScope.launch {
            _errorEvent.emit(ErrorEventData(message, action))
        }
    }
}