package com.mustafacanyucel.fireflyiiishortcuts.ui.shortcuts

data class UiState(
    val shortcuts: List<ShortcutWithState> = emptyList(),
    val isBusy: Boolean = false,
)
