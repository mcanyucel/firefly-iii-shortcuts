package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel

data class UiState(
    val shortcuts: List<ShortcutModel> = emptyList(),
    val isBusy: Boolean = false,
)
