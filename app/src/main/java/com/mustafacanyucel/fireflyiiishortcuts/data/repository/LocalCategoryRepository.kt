package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.CategoryDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.CategoryData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : ILocalCategoryRepository {
    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    override suspend fun getCategoryById(id: Int): CategoryEntity? {
        return categoryDao.getCategoryById(id)
    }

    override suspend fun saveCategories(categories: List<CategoryData>): Int {
        try {
            Log.d(TAG, "Saving ${categories.size} categories to the database")
            val entities = categories.map { CategoryEntity.fromApiModel(it) }

            categoryDao.replaceAllCategories(entities)
            Log.d(TAG, "Successfully saved ${categories.size} categories to the database")
            return categories.size
        } catch (e: Exception) {
            Log.e(TAG, "Error saving categories to the database", e)
            throw e
        }
    }

    override suspend fun getCategoryCount(): Int {
        return categoryDao.getCategoriesCount()
    }

    companion object {
        private const val TAG = "LocalCategoryRepository"
    }
}