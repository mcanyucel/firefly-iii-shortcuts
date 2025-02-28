package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import kotlinx.coroutines.flow.Flow

interface ILocalTagRepository {
    suspend fun saveTags(tags: List<TagEntity>)
    suspend fun getAllTags(): List<TagEntity>
    suspend fun getTagById(id: String): TagEntity?
    suspend fun insertTag(tag: TagEntity): String
    suspend fun updateTag(tag: TagEntity): Int
    suspend fun deleteTag(id: String): Int
    fun observeAllTags(): Flow<List<TagEntity>>
}