package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the budgets table
 */
@Dao
interface BudgetDao {
    /**
     * Insert a single budget
     * @return the row ID of the inserted budget
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budgetEntity: BudgetEntity): Long

    /**
     * Insert multiple budgets
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgets(budgetEntities: List<BudgetEntity>)

    /**
     * Update an existing budget
     * @return the number of budgets updated (should be 1)
     */
    @Update
    suspend fun updateBudget(budgetEntity: BudgetEntity): Int

    /**
     * Get a single budget by ID
     */
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: String): BudgetEntity?

    /**
     * Get all budgets
     */
    @Query("SELECT * FROM budgets ORDER BY name")
    suspend fun getAllBudgets(): List<BudgetEntity>

    /**
     * Delete a budget by ID
     * @return the number of budgets deleted (should be 1)
     */
    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteBudgetById(id: String): Int

    /**
     * Delete all budgets
     */
    @Query("DELETE FROM budgets")
    suspend fun deleteAllBudgets()

    /**
     * Observe all budgets as a Flow
     */
    @Query("SELECT * FROM budgets ORDER BY name")
    fun observeAllBudgets(): Flow<List<BudgetEntity>>


    /**
     * Transaction to replace all budgets
     * This is useful for syncing data from the server
     */
    @Transaction
    suspend fun replaceAllBudgets(budgets: List<BudgetEntity>) {
        deleteAllBudgets()
        insertBudgets(budgets)
    }
}