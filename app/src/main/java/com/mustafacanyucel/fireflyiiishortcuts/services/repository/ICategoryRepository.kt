package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import com.mustafacanyucel.fireflyiiishortcuts.model.api.category.CategoryData
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    suspend fun getCategories(): Flow<ApiResult<List<CategoryData>>>
}