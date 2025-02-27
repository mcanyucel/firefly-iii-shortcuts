package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BudgetDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.BudgetData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) : ILocalBudgetRepository {
    override fun getAllBudgets(): Flow<List<BudgetEntity>> {
        return budgetDao.getAllBudgets()
    }

    override suspend fun getBudgetById(id: String): BudgetEntity? {
        return budgetDao.getBudgetById(id)
    }

    override suspend fun saveBudgets(budgets: List<BudgetData>): Int {
        try {
            Log.d(TAG, "Saving ${budgets.size} budgets to the database")
            val entities = budgets.map { BudgetEntity.fromApiModel(it) }

            budgetDao.replaceAllBudgets(entities)
            Log.d(TAG, "Successfully saved ${budgets.size} budgets to the database")
            return entities.size
        } catch (e: Exception) {
            Log.e(TAG, "Error saving budgets to the database", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalBudgetRepository"
    }
}