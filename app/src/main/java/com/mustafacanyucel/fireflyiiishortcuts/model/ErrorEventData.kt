package com.mustafacanyucel.fireflyiiishortcuts.model

data class ErrorEventData(val message: String, val action: (()->Unit)? = null)