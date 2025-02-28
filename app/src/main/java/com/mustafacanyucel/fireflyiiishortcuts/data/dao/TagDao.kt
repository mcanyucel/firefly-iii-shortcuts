package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the tags table
 */
@Dao
interface TagDao {
    /**
     * Insert a single tag
     * @return the row ID of the inserted tag
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagEntity: TagEntity): Long

    /**
     * Insert multiple tags
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tagEntities: List<TagEntity>)

    /**
     * Update an existing tag
     * @return the number of tags updated (should be 1)
     */
    @Update
    suspend fun updateTag(tagEntity: TagEntity): Int

    /**
     * Get a single tag by ID
     */
    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: String): TagEntity?

    /**
     * Get all tags
     */
    @Query("SELECT * FROM tags ORDER BY tag")
    suspend fun getAllTags(): List<TagEntity>

    /**
     * Delete a tag by ID
     * @return the number of tags deleted (should be 1)
     */
    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun deleteTagById(id: String): Int

    /**
     * Delete all tags
     */
    @Query("DELETE FROM tags")
    suspend fun deleteAllTags()

    /**
     * Observe all tags as a Flow
     */
    @Query("SELECT * FROM tags ORDER BY tag")
    fun observeAllTags(): Flow<List<TagEntity>>


    /**
     * Transaction to replace all tags
     * This is useful for syncing data from the server
     */
    @Transaction
    suspend fun replaceAllTags(tags: List<TagEntity>) {
        deleteAllTags()
        insertTags(tags)
    }
}