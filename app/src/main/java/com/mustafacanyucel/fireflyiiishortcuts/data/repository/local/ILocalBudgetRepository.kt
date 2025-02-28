package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

interface ILocalBudgetRepository {
    suspend fun saveBudgets(budgets: List<BudgetEntity>)
    suspend fun getAllBudgets(): List<BudgetEntity>
    suspend fun getBudgetById(id: String): BudgetEntity?
    suspend fun insertBudget(budget: BudgetEntity): Long
    suspend fun updateBudget(budget: BudgetEntity): Int
    suspend fun deleteBudget(id: String): Int
    fun observeAllBudgets(): Flow<List<BudgetEntity>>
}