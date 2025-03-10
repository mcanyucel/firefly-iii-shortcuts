package com.mustafacanyucel.fireflyiiishortcuts.ui.model

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutWithTags
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TransactionType
import java.math.BigDecimal

/**
 * Represents a shortcut model that is used in the UI layer
 */
data class ShortcutModel(
    val id: Long,
    val name: String,
    val amount: BigDecimal,
    val transactionType: TransactionType,
    val fromAccountEntity: AccountEntity?,
    val toAccountEntity: AccountEntity?,
    val categoryEntity: CategoryEntity?,
    val budgetEntity: BudgetEntity?,
    val billEntity: BillEntity?,
    val piggybankEntity: PiggybankEntity?,
    val description: String?,
    val icon: String?,
    val tagEntities: List<TagEntity>,
    val lastUsed: Long? = null
) {
    companion object {
        fun fromEntity(
            shortcutWithTagsEntity: ShortcutWithTags,
            fromAccountEntity: AccountEntity?,
            toAccountEntity: AccountEntity?,
            categoryEntity: CategoryEntity?,
            budgetEntity: BudgetEntity?,
            billEntity: BillEntity?,
            piggybankEntity: PiggybankEntity?,
        ): ShortcutModel {
            return ShortcutModel(
                id = shortcutWithTagsEntity.shortcut.id,
                name = shortcutWithTagsEntity.shortcut.name,
                amount = shortcutWithTagsEntity.shortcut.amount,
                transactionType = shortcutWithTagsEntity.shortcut.transactionType,
                fromAccountEntity = fromAccountEntity,
                toAccountEntity = toAccountEntity,
                categoryEntity = categoryEntity,
                budgetEntity = budgetEntity,
                billEntity = billEntity,
                piggybankEntity = piggybankEntity,
                description = shortcutWithTagsEntity.shortcut.description,
                icon = shortcutWithTagsEntity.shortcut.icon,
                tagEntities = shortcutWithTagsEntity.tags,
                lastUsed = shortcutWithTagsEntity.shortcut.lastUsed
            )
        }

        fun createNew(): ShortcutModel {
            return ShortcutModel(
                id = 0,
                name = "",
                amount = BigDecimal.ZERO,
                transactionType = TransactionType.WITHDRAWAL,
                fromAccountEntity = null,
                toAccountEntity = null,
                categoryEntity = null,
                budgetEntity = null,
                billEntity = null,
                piggybankEntity = null,
                description = null,
                icon = null,
                tagEntities = emptyList(),
                lastUsed = null
            )
        }
    }
}