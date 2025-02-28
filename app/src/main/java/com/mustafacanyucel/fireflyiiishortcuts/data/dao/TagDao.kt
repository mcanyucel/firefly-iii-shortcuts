package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    /**
     * Get all tags as a flow
     */
    @Query("SELECT * FROM tags ORDER BY tag ASC")
    fun getAllTags(): Flow<List<TagEntity>>

    /**
     * Get a single tag by id
     */
    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: Int): TagEntity?

    /**
     * Insert a single tag
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity)

    /**
     * Insert multiple tags
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    /**
     * Delete all tags
     */
    @Query("DELETE FROM tags")
    suspend fun deleteAllTags()

    /**
     * Replaces all tags in a single transaction.
     */
    @Transaction
    suspend fun replaceAllTags(tags: List<TagEntity>) {
        deleteAllTags()
        insertTags(tags)
    }
}