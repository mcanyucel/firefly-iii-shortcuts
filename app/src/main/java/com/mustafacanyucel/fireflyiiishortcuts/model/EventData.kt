package com.mustafacanyucel.fireflyiiishortcuts.model

data class EventData(
    val eventType: EventType,
    val message: String,
    val action: (() -> Unit)? = null,
    val actionTitle: String? = null
)
