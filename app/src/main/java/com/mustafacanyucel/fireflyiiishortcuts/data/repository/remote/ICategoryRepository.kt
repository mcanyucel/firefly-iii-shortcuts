package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.model.api.category.CategoryData
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    suspend fun getCategories(): Flow<ApiResult<List<CategoryData>>>
}