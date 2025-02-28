package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.tag.TagData
import kotlinx.coroutines.flow.Flow

interface IRemoteTagRepository {
    suspend fun getTags(): Flow<ApiResult<List<TagEntity>>>
}