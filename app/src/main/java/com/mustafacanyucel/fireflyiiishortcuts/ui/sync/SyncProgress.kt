package com.mustafacanyucel.fireflyiiishortcuts.ui.sync

data class SyncProgress(
    val step: SyncStep,
    val message: String,
    val errorDetails: String? = null
)
