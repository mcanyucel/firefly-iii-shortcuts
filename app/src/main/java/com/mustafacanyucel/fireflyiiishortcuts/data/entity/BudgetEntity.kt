package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustafacanyucel.fireflyiiishortcuts.model.api.BudgetData

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val active: Boolean,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String,
    val currencyId: String?,
    val currencyCode: String?,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromApiModel(budgetData: BudgetData): BudgetEntity {
            return BudgetEntity(
                id = budgetData.id,
                name = budgetData.attributes.name,
                active = budgetData.attributes.active,
                notes = budgetData.attributes.notes,
                createdAt = budgetData.attributes.createdAt,
                updatedAt = budgetData.attributes.updatedAt,
                currencyId = budgetData.attributes.currencyId,
                currencyCode = budgetData.attributes.currencyCode
            )
        }
    }
}