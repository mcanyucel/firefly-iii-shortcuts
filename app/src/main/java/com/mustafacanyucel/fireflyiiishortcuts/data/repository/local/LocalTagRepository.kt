package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.TagDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTagRepository @Inject constructor(
    private val tagDao: TagDao
) : ILocalTagRepository {

    /**
     * Save a list of tags to the database.
     * This implementation uses a transaction to replace all tags at once.
     */
    override suspend fun saveTags(tags: List<TagEntity>) {
        try {
            // Use a transaction to ensure data consistency
            tagDao.replaceAllTags(tags)
            Log.d(TAG, "Saved ${tags.size} tags to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving tags to database", e)
            throw e
        }
    }

    /**
     * Get all tags from the database
     */
    override suspend fun getAllTags(): List<TagEntity> {
        return try {
            val tags = tagDao.getAllTags()
            Log.d(TAG, "Retrieved ${tags.size} tags from database")
            tags
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving tags from database", e)
            throw e
        }
    }

    /**
     * Get a tag by its ID
     */
    override suspend fun getTagById(id: String): TagEntity? {
        return try {
            val tag = tagDao.getTagById(id)
            if (tag != null) {
                Log.d(TAG, "Retrieved tag with id $id")
            } else {
                Log.d(TAG, "No tag found with id $id")
            }
            tag
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving tag with id $id", e)
            throw e
        }
    }

    /**
     * Insert a new tag and return its ID
     * Note: Since the interface specifies returning a String ID rather than the Long row ID
     * that Room provides, we'll return the tag's ID directly
     */
    override suspend fun insertTag(tag: TagEntity): String {
        return try {
            // Insert and ignore the returned row ID
            tagDao.insertTag(tag)
            Log.d(TAG, "Inserted tag with id ${tag.id}")
            tag.id
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting tag with id ${tag.id}", e)
            throw e
        }
    }

    /**
     * Update an existing tag
     * @return Number of rows updated (should be 1 if successful)
     */
    override suspend fun updateTag(tag: TagEntity): Int {
        return try {
            val updatedRows = tagDao.updateTag(tag)
            Log.d(TAG, "Updated $updatedRows tag(s) with id ${tag.id}")
            updatedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error updating tag with id ${tag.id}", e)
            throw e
        }
    }

    /**
     * Delete a tag by its ID
     * @return Number of rows deleted (should be 1 if successful)
     */
    override suspend fun deleteTag(id: String): Int {
        return try {
            val deletedRows = tagDao.deleteTagById(id)
            Log.d(TAG, "Deleted $deletedRows tag(s) with id $id")
            deletedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting tag with id $id", e)
            throw e
        }
    }

    /**
     * Observe all tags and emit updates when the data changes
     */
    override fun observeAllTags(): Flow<List<TagEntity>> {
        return try {
            tagDao.observeAllTags()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing tags", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalTagRepository"
    }
}