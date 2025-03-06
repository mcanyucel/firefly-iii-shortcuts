package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel

data class UiState(
    val shortcuts: List<ShortcutModel> = emptyList(),
    val isLoading: Boolean = false,
)
