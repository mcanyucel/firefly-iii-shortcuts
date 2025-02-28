package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.CategoryDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : ILocalCategoryRepository {

    /**
     * Save a list of categories to the database.
     * This implementation uses a transaction to replace all categories at once.
     */
    override suspend fun saveCategories(categories: List<CategoryEntity>) {
        try {
            // Use a transaction to ensure data consistency
            categoryDao.replaceAllCategories(categories)
            Log.d(TAG, "Saved ${categories.size} categories to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving categories to database", e)
            throw e
        }
    }

    /**
     * Get all categories from the database
     */
    override suspend fun getAllCategories(): List<CategoryEntity> {
        return try {
            val categories = categoryDao.getAllCategories()
            Log.d(TAG, "Retrieved ${categories.size} categories from database")
            categories
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving categories from database", e)
            throw e
        }
    }

    /**
     * Get a category by its ID
     */
    override suspend fun getCategoryById(id: String): CategoryEntity? {
        return try {
            val category = categoryDao.getCategoryById(id)
            if (category != null) {
                Log.d(TAG, "Retrieved category with id $id")
            } else {
                Log.d(TAG, "No category found with id $id")
            }
            category
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving category with id $id", e)
            throw e
        }
    }

    /**
     * Insert a new category and return its ID
     * Note: Since the interface specifies returning a String ID rather than the Long row ID
     * that Room provides, we'll return the category's ID directly
     */
    override suspend fun insertCategory(category: CategoryEntity): String {
        return try {
            // We ignore the returned row ID and just return the predefined category ID
            categoryDao.insertCategory(category)
            Log.d(TAG, "Inserted category with id ${category.id}")
            category.id
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting category with id ${category.id}", e)
            throw e
        }
    }

    /**
     * Update an existing category
     * @return Number of rows updated (should be 1 if successful)
     */
    override suspend fun updateCategory(category: CategoryEntity): Int {
        return try {
            val updatedRows = categoryDao.updateCategory(category)
            Log.d(TAG, "Updated $updatedRows category/categories with id ${category.id}")
            updatedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error updating category with id ${category.id}", e)
            throw e
        }
    }

    /**
     * Delete a category by its ID
     * @return Number of rows deleted (should be 1 if successful)
     */
    override suspend fun deleteCategory(id: String): Int {
        return try {
            val deletedRows = categoryDao.deleteCategoryById(id)
            Log.d(TAG, "Deleted $deletedRows category/categories with id $id")
            deletedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting category with id $id", e)
            throw e
        }
    }

    /**
     * Observe all categories and emit updates when the data changes
     */
    override fun observeAllCategories(): Flow<List<CategoryEntity>> {
        return try {
            categoryDao.observeAllCategories()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing categories", e)
            throw e
        }
    }

    /**
     * Search for categories by name
     * @param query The search term to find in category names
     * @return A list of matching categories
     */
    suspend fun searchCategories(query: String): List<CategoryEntity> {
        return try {
            if (query.isBlank()) {
                emptyList()
            } else {
                val categories = categoryDao.searchCategoriesByName(query)
                Log.d(TAG, "Found ${categories.size} categories matching '$query'")
                categories
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error searching for categories with query '$query'", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalCategoryRepository"
    }
}