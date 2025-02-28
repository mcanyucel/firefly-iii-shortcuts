package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface ILocalCategoryRepository {
    suspend fun saveCategories(categories: List<CategoryEntity>)
    suspend fun getAllCategories(): List<CategoryEntity>
    suspend fun getCategoryById(id: String): CategoryEntity?
    suspend fun insertCategory(category: CategoryEntity): String
    suspend fun updateCategory(category: CategoryEntity): Int
    suspend fun deleteCategory(id: String): Int
    fun observeAllCategories(): Flow<List<CategoryEntity>>
}