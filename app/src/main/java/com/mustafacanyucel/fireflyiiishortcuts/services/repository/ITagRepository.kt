package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import com.mustafacanyucel.fireflyiiishortcuts.model.api.tag.TagData
import kotlinx.coroutines.flow.Flow

interface ITagRepository {
    suspend fun getTags(): Flow<ApiResult<List<TagData>>>
}