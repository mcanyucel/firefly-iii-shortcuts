package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BudgetDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) : ILocalBudgetRepository {

    /**
     * Save a list of budgets to the database.
     * This implementation uses a transaction to replace all budgets at once.
     */
    override suspend fun saveBudgets(budgets: List<BudgetEntity>) {
        try {
            // Use a transaction to ensure data consistency
            budgetDao.replaceAllBudgets(budgets)
            Log.d(TAG, "Saved ${budgets.size} budgets to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving budgets to database", e)
            throw e
        }
    }

    /**
     * Get all budgets from the database
     */
    override suspend fun getAllBudgets(): List<BudgetEntity> {
        return try {
            val budgets = budgetDao.getAllBudgets()
            Log.d(TAG, "Retrieved ${budgets.size} budgets from database")
            budgets
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving budgets from database", e)
            throw e
        }
    }

    /**
     * Get a budget by its ID
     */
    override suspend fun getBudgetById(id: String): BudgetEntity? {
        return try {
            val budget = budgetDao.getBudgetById(id)
            if (budget != null) {
                Log.d(TAG, "Retrieved budget with id $id")
            } else {
                Log.d(TAG, "No budget found with id $id")
            }
            budget
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving budget with id $id", e)
            throw e
        }
    }

    /**
     * Insert a new budget and return its row ID
     * Note: Since we're inserting with a predefined String ID,
     * the returned Long value is just the row ID, not the actual ID of the budget
     */
    override suspend fun insertBudget(budget: BudgetEntity): Long {
        return try {
            val insertedRowId = budgetDao.insertBudget(budget)
            Log.d(TAG, "Inserted budget with id ${budget.id}, row ID: $insertedRowId")
            insertedRowId
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting budget with id ${budget.id}", e)
            throw e
        }
    }

    /**
     * Update an existing budget
     * @return Number of rows updated (should be 1 if successful)
     */
    override suspend fun updateBudget(budget: BudgetEntity): Int {
        return try {
            val updatedRows = budgetDao.updateBudget(budget)
            Log.d(TAG, "Updated $updatedRows budget(s) with id ${budget.id}")
            updatedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error updating budget with id ${budget.id}", e)
            throw e
        }
    }

    /**
     * Delete a budget by its ID
     * @return Number of rows deleted (should be 1 if successful)
     */
    override suspend fun deleteBudget(id: String): Int {
        return try {
            val deletedRows = budgetDao.deleteBudgetById(id)
            Log.d(TAG, "Deleted $deletedRows budget(s) with id $id")
            deletedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting budget with id $id", e)
            throw e
        }
    }

    /**
     * Observe all budgets and emit updates when the data changes
     */
    override fun observeAllBudgets(): Flow<List<BudgetEntity>> {
        return try {
            budgetDao.observeAllBudgets()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing budgets", e)
            throw e
        }
    }


    companion object {
        private const val TAG = "LocalBudgetRepository"
    }
}