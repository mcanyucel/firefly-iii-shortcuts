package com.mustafacanyucel.fireflyiiishortcuts.ui.management.model

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutWithTags
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import java.math.BigDecimal

/**
 * Represents a shortcut model that is used in the UI layer
 */
data class ShortcutModel(
    val id: Long,
    val name: String,
    val amount: BigDecimal,
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

    fun toEntity(): ShortcutEntity {
        if (fromAccountEntity == null || toAccountEntity == null) {
            throw IllegalArgumentException("fromAccountEntity and toAccountEntity cannot be null")
        }
        return ShortcutEntity(
            id = id,
            name = name,
            amount = amount,
            fromAccountId = fromAccountEntity.id,
            toAccountId = toAccountEntity.id,
            categoryId = categoryEntity?.id,
            budgetId = budgetEntity?.id,
            billId = billEntity?.id,
            piggybankId = piggybankEntity?.id,
            description = description,
            icon = icon,
            lastUsed = lastUsed
        )
    }

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