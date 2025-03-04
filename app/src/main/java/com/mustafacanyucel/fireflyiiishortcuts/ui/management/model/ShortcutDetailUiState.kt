package com.mustafacanyucel.fireflyiiishortcuts.ui.management.model

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity

data class ShortcutDetailUiState(
    val draftShortcut: ShortcutModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val accounts: List<AccountEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val tags: List<TagEntity> = emptyList(),
    val bills: List<BillEntity> = emptyList(),
    val budgets: List<BudgetEntity> = emptyList(),
    val piggybanks: List<PiggybankEntity> = emptyList(),
)