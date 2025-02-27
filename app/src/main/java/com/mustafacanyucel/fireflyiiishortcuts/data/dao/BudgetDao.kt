package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    /**
     * Get all budgets as a Flow
     */
    @Query("SELECT * FROM budgets ORDER BY name ASC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    /**
     * Get a single budget by id
     */
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: String): BudgetEntity?

    /**
     * Insert a budget
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    /**
     * Insert multiple budgets
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgets(budgets: List<BudgetEntity>)

    /**
     * Delete all budgets
     */
    @Query("DELETE FROM budgets")
    suspend fun deleteAllBudgets()

    /**
     * Replace all budgets in a single transaction
     */
    @Transaction
    suspend fun replaceAllBudgets(budgets: List<BudgetEntity>) {
        deleteAllBudgets()
        insertBudgets(budgets)
    }
}