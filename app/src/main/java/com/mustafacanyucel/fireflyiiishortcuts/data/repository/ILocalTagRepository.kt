package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.tag.TagData
import kotlinx.coroutines.flow.Flow

interface ILocalTagRepository {
    fun getAllTags(): Flow<List<TagEntity>>
    suspend fun getTagById(id: Int): TagEntity?
    suspend fun saveTags(tags: List<TagData>): Int
}