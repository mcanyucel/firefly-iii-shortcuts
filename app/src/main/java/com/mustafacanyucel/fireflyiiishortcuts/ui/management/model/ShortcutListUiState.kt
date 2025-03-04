package com.mustafacanyucel.fireflyiiishortcuts.ui.management.model

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutWithTags

data class ShortcutListUiState(
    val shortcuts: List<ShortcutModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
