package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.category.CategoryData
import kotlinx.coroutines.flow.Flow

interface ILocalCategoryRepository {
    fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun getCategoryById(id: Int): CategoryEntity?
    suspend fun saveCategories(categories: List<CategoryData>): Int
    suspend fun getCategoryCount(): Int

}