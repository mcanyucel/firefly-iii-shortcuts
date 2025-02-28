package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the categories table
 */
@Dao
interface CategoryDao {
    /**
     * Insert a single category
     * @return the row ID of the inserted category
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity): Long

    /**
     * Insert multiple categories
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categoryEntities: List<CategoryEntity>)

    /**
     * Update an existing category
     * @return the number of categories updated (should be 1)
     */
    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity): Int

    /**
     * Get a single category by ID
     */
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    /**
     * Get all categories
     */
    @Query("SELECT * FROM categories ORDER BY name")
    suspend fun getAllCategories(): List<CategoryEntity>

    /**
     * Delete a category by ID
     * @return the number of categories deleted (should be 1)
     */
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: String): Int

    /**
     * Delete all categories
     */
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    /**
     * Observe all categories as a Flow
     */
    @Query("SELECT * FROM categories ORDER BY name")
    fun observeAllCategories(): Flow<List<CategoryEntity>>

    /**
     * Get categories containing a search term in their name
     */
    @Query("SELECT * FROM categories WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name")
    suspend fun searchCategoriesByName(searchTerm: String): List<CategoryEntity>


    /**
     * Transaction to replace all categories
     * This is useful for syncing data from the server
     */
    @Transaction
    suspend fun replaceAllCategories(categories: List<CategoryEntity>) {
        deleteAllCategories()
        insertCategories(categories)
    }
}