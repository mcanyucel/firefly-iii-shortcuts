package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutTagCrossRef
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutWithTags
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the shortcuts table and related operations
 */
@Dao
interface ShortcutDao {
    /**
     * Insert a single shortcut
     * @return the row ID of the inserted shortcut
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcut(shortcutEntity: ShortcutEntity): Long

    /**
     * Insert multiple shortcuts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcuts(shortcutEntities: List<ShortcutEntity>)

    /**
     * Update an existing shortcut
     * @return the number of shortcuts updated (should be 1)
     */
    @Update
    suspend fun updateShortcut(shortcutEntity: ShortcutEntity): Int

    /**
     * Get a single shortcut by ID
     */
    @Query("SELECT * FROM shortcuts WHERE id = :id")
    suspend fun getShortcutById(id: Long): ShortcutEntity?

    /**
     * Get all shortcuts
     */
    @Query("SELECT * FROM shortcuts ORDER BY name")
    suspend fun getAllShortcuts(): List<ShortcutEntity>

    /**
     * Delete a shortcut by ID
     * @return the number of shortcuts deleted (should be 1)
     */
    @Query("DELETE FROM shortcuts WHERE id = :id")
    suspend fun deleteShortcutById(id: Long): Int

    /**
     * Delete a shortcut
     */
    @Delete
    suspend fun deleteShortcut(shortcutEntity: ShortcutEntity): Int

    /**
     * Delete all shortcuts
     */
    @Query("DELETE FROM shortcuts")
    suspend fun deleteAllShortcuts()

    /**
     * Observe all shortcuts as a Flow
     */
    @Query("SELECT * FROM shortcuts ORDER BY name")
    fun observeAllShortcuts(): Flow<List<ShortcutEntity>>

    /**
     * Get shortcuts by source account ID
     */
    @Query("SELECT * FROM shortcuts WHERE fromAccountId = :accountId ORDER BY name")
    suspend fun getShortcutsByFromAccountId(accountId: String): List<ShortcutEntity>

    /**
     * Get shortcuts by destination account ID
     */
    @Query("SELECT * FROM shortcuts WHERE toAccountId = :accountId ORDER BY name")
    suspend fun getShortcutsByToAccountId(accountId: String): List<ShortcutEntity>

    /**
     * Get shortcuts by category ID
     */
    @Query("SELECT * FROM shortcuts WHERE categoryId = :categoryId ORDER BY name")
    suspend fun getShortcutsByCategoryId(categoryId: String): List<ShortcutEntity>

    /**
     * Get shortcuts by budget ID
     */
    @Query("SELECT * FROM shortcuts WHERE budgetId = :budgetId ORDER BY name")
    suspend fun getShortcutsByBudgetId(budgetId: String): List<ShortcutEntity>

    /**
     * Get shortcuts by bill ID
     */
    @Query("SELECT * FROM shortcuts WHERE billId = :billId ORDER BY name")
    suspend fun getShortcutsByBillId(billId: String): List<ShortcutEntity>

    /**
     * Get shortcuts by piggybank ID
     */
    @Query("SELECT * FROM shortcuts WHERE piggybankId = :piggybankId ORDER BY name")
    suspend fun getShortcutsByPiggybankId(piggybankId: String): List<ShortcutEntity>

    /**
     * Update shortcut last used timestamp
     */
    @Query("UPDATE shortcuts SET lastUsed = :timestamp WHERE id = :id")
    suspend fun updateShortcutLastUsed(id: Long, timestamp: Long)

    // Tag relationship methods

    /**
     * Insert a tag reference for a shortcut
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcutTagCrossRef(crossRef: ShortcutTagCrossRef)

    /**
     * Insert multiple tag references for shortcuts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcutTagCrossRefs(crossRefs: List<ShortcutTagCrossRef>)

    /**
     * Delete a tag reference for a shortcut
     */
    @Delete
    suspend fun deleteShortcutTagCrossRef(crossRef: ShortcutTagCrossRef)

    /**
     * Delete all tag references for a shortcut
     */
    @Query("DELETE FROM shortcut_tag_cross_ref WHERE shortcutId = :shortcutId")
    suspend fun deleteTagsForShortcut(shortcutId: Long)

    /**
     * Get shortcuts by tag ID
     */
    @Query(
        """
        SELECT s.* FROM shortcuts s
        INNER JOIN shortcut_tag_cross_ref ref ON s.id = ref.shortcutId
        WHERE ref.tagId = :tagId
        ORDER BY s.name
    """
    )
    suspend fun getShortcutsByTagId(tagId: String): List<ShortcutEntity>

    /**
     * Get shortcut with all its tags
     */
    @Transaction
    @Query(
        """
        SELECT * FROM shortcuts WHERE id = :shortcutId
    """
    )
    suspend fun getShortcutWithTags(shortcutId: Long): ShortcutWithTags?

    /**
     * Get all shortcuts with their tags
     */
    @Transaction
    @Query("SELECT * FROM shortcuts ORDER BY name")
    suspend fun getAllShortcutsWithTags(): List<ShortcutWithTags>

    /**
     * Observe all shortcuts with their tags as a Flow
     */
    @Transaction
    @Query("SELECT * FROM shortcuts ORDER BY name")
    fun observeAllShortcutsWithTags(): Flow<List<ShortcutWithTags>>

    /**
     * Transaction to add or update a shortcut with its tags
     */
    @Transaction
    suspend fun saveShortcutWithTags(shortcut: ShortcutEntity, tagIds: List<String>): Long {
        // Insert or update the shortcut
        val shortcutId = if (shortcut.id == 0L) {
            insertShortcut(shortcut)
        } else {
            updateShortcut(shortcut)
            shortcut.id
        }

        // Clear existing tag associations
        deleteTagsForShortcut(shortcutId)

        // Add new tag associations
        val tagRefs = tagIds.map { tagId ->
            ShortcutTagCrossRef(shortcutId, tagId)
        }

        if (tagRefs.isNotEmpty()) {
            insertShortcutTagCrossRefs(tagRefs)
        }

        return shortcutId
    }
}