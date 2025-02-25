package com.mustafacanyucel.fireflyiiishortcuts.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.model.EventData
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class ViewModelBase : ViewModel() {
    private val _eventData = MutableSharedFlow<EventData>()
    val eventDataSharedFlow = _eventData.asSharedFlow()

    protected fun emitEvent(eventType: EventType, message: String, action: (() -> Unit)? = null) {
        viewModelScope.launch {
            _eventData.emit(EventData(eventType, message, action))
        }
    }
}