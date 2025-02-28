package com.mustafacanyucel.fireflyiiishortcuts.ui.sync

enum class SyncStep {
    IDLE,
    FETCHING_REMOTE,
    FETCHING_LOCAL,
    COMPARING_DATA,
    APPLYING_CHANGES,
    COMPLETED,
    ERROR
}