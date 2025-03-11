package com.mustafacanyucel.fireflyiiishortcuts.widget

import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import java.math.BigDecimal

/**
 * Lightweight data class for widget items, containing the minimum information needed.
 */
data class ShortcutWidgetItem(
    val id: Long,
    val name: String,
    val amount: BigDecimal,
    val fromAccountName: String,
    val toAccountName: String,
    val state: ShortcutState
)
