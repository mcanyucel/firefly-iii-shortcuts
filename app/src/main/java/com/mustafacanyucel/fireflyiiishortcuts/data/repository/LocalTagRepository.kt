package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.TagDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.tag.TagData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTagRepository @Inject constructor(
    private val tagDao: TagDao
) : ILocalTagRepository {
    override fun getAllTags(): Flow<List<TagEntity>> {
        return tagDao.getAllTags()
    }

    override suspend fun getTagById(id: Int): TagEntity? {
        return tagDao.getTagById(id)
    }

    override suspend fun saveTags(tags: List<TagData>): Int {
        try {
            val entities = tags.map { TagEntity.fromApiModel(it) }
            tagDao.replaceAllTags(entities)
            return entities.size
        } catch (e: Exception) {
            Log.e(TAG, "Error saving tags: to the database", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalTagRepository"
    }
}