package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.category.CategoryData
import kotlinx.coroutines.flow.Flow

interface IRemoteCategoryRepository {
    suspend fun getCategories(): Flow<ApiResult<List<CategoryEntity>>>
}