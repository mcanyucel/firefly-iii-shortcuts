package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.budget.BudgetData
import kotlinx.coroutines.flow.Flow

interface ILocalBudgetRepository {
    fun getAllBudgets(): Flow<List<BudgetEntity>>
    suspend fun getBudgetById(id: String): BudgetEntity?
    suspend fun saveBudgets(budgets: List<BudgetData>): Int
}