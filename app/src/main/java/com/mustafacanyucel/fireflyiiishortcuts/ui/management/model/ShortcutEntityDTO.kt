package com.mustafacanyucel.fireflyiiishortcuts.ui.management.model

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TransactionType
import java.math.BigDecimal

data class ShortcutEntityDTO(
    val name: String,
    val amount: BigDecimal,
    val fromAccountId: String,
    val toAccountId: String,
    val categoryId: String? = null, // Optional: a shortcut may not have a category
    val budgetId: String? = null, // Optional: a shortcut may not have a budget
    val billId: String? = null, // Optional: a shortcut may not have a bill
    val piggybankId: String? = null, // Optional: a shortcut may not have a piggybank
    val icon: String? = null, // Optional: for UI display
    val description: String? = null,
    val tagIds: List<String> = emptyList()
) {
    fun toEntity(id: Long = 0, type: TransactionType = TransactionType.WITHDRAWAL): ShortcutEntity {
        return ShortcutEntity(
            id = id,
            name = name,
            amount = amount,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            categoryId = categoryId,
            budgetId = budgetId,
            billId = billId,
            piggybankId = piggybankId,
            icon = icon,
            description = description,
            transactionType = type,
        )
    }
}