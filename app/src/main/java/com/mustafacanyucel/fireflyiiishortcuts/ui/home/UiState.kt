package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel

data class UiState(
    val shortcuts: List<ShortcutWithState> = emptyList(),
    val isBusy: Boolean = false,
)
