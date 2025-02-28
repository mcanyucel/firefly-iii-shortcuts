package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.ShortcutDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutWithTags
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ILocalShortcutRepository that uses Room database for storage
 */
@Singleton
class LocalShortcutRepository @Inject constructor(
    private val shortcutDao: ShortcutDao
) : ILocalShortcutRepository {

    /**
     * Save a list of shortcuts to the database.
     * Note: This method only saves the shortcuts, not their tag associations.
     * For managing tag associations, use saveShortcutWithTags.
     */
    override suspend fun saveShortcuts(shortcuts: List<ShortcutEntity>) {
        try {
            shortcutDao.insertShortcuts(shortcuts)
            Log.d(TAG, "Saved ${shortcuts.size} shortcuts to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving shortcuts to database", e)
            throw e
        }
    }

    /**
     * Get all shortcuts from the database
     */
    override suspend fun getAllShortcuts(): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getAllShortcuts()
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts from database")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts from database", e)
            throw e
        }
    }

    /**
     * Get a shortcut by its ID
     */
    override suspend fun getShortcutById(id: Long): ShortcutEntity? {
        return try {
            val shortcut = shortcutDao.getShortcutById(id)
            if (shortcut != null) {
                Log.d(TAG, "Retrieved shortcut with id $id")
            } else {
                Log.d(TAG, "No shortcut found with id $id")
            }
            shortcut
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcut with id $id", e)
            throw e
        }
    }

    /**
     * Insert a new shortcut and return its ID
     */
    override suspend fun insertShortcut(shortcut: ShortcutEntity): Long {
        return try {
            val insertedId = shortcutDao.insertShortcut(shortcut)
            Log.d(TAG, "Inserted shortcut with id $insertedId")
            insertedId
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting shortcut", e)
            throw e
        }
    }

    /**
     * Update an existing shortcut
     * @return Number of rows updated (should be 1 if successful)
     */
    override suspend fun updateShortcut(shortcut: ShortcutEntity): Int {
        return try {
            val updatedRows = shortcutDao.updateShortcut(shortcut)
            Log.d(TAG, "Updated $updatedRows shortcut(s) with id ${shortcut.id}")
            updatedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error updating shortcut with id ${shortcut.id}", e)
            throw e
        }
    }

    /**
     * Delete a shortcut by its ID
     * @return Number of rows deleted (should be 1 if successful)
     */
    override suspend fun deleteShortcut(id: Long): Int {
        return try {
            val deletedRows = shortcutDao.deleteShortcutById(id)
            Log.d(TAG, "Deleted $deletedRows shortcut(s) with id $id")
            deletedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting shortcut with id $id", e)
            throw e
        }
    }

    /**
     * Observe all shortcuts and emit updates when the data changes
     */
    override fun observeAllShortcuts(): Flow<List<ShortcutEntity>> {
        return try {
            shortcutDao.observeAllShortcuts()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing shortcuts", e)
            throw e
        }
    }

    /**
     * Save a shortcut with its tag associations
     * This method handles both insert and update operations, as well as managing tag associations
     */
    override suspend fun saveShortcutWithTags(
        shortcut: ShortcutEntity, tagIds: List<String>
    ): Long {
        return try {
            val shortcutId = shortcutDao.saveShortcutWithTags(shortcut, tagIds)
            Log.d(TAG, "Saved shortcut with id $shortcutId and ${tagIds.size} tags")
            shortcutId
        } catch (e: Exception) {
            Log.e(TAG, "Error saving shortcut with tags", e)
            throw e
        }
    }

    /**
     * Get a shortcut with all its tags
     */
    override suspend fun getShortcutWithTags(shortcutId: Long): ShortcutWithTags? {
        return try {
            val shortcutWithTags = shortcutDao.getShortcutWithTags(shortcutId)
            if (shortcutWithTags != null) {
                Log.d(
                    TAG, "Retrieved shortcut id $shortcutId with ${shortcutWithTags.tags.size} tags"
                )
            } else {
                Log.d(TAG, "No shortcut found with id $shortcutId")
            }
            shortcutWithTags
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcut with tags for id $shortcutId", e)
            throw e
        }
    }

    /**
     * Get all shortcuts with their tags
     */
    override suspend fun getAllShortcutsWithTags(): List<ShortcutWithTags> {
        return try {
            val shortcutsWithTags = shortcutDao.getAllShortcutsWithTags()
            Log.d(TAG, "Retrieved ${shortcutsWithTags.size} shortcuts with tags")
            shortcutsWithTags
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with tags", e)
            throw e
        }
    }

    /**
     * Observe all shortcuts with their tags
     */
    override fun observeAllShortcutsWithTags(): Flow<List<ShortcutWithTags>> {
        return try {
            shortcutDao.observeAllShortcutsWithTags()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing shortcuts with tags", e)
            throw e
        }
    }

    /**
     * Get shortcuts by source account ID
     */
    override suspend fun getShortcutsByFromAccountId(accountId: String): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getShortcutsByFromAccountId(accountId)
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts with from account id $accountId")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with from account id $accountId", e)
            throw e
        }
    }

    /**
     * Get shortcuts by destination account ID
     */
    override suspend fun getShortcutsByToAccountId(accountId: String): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getShortcutsByToAccountId(accountId)
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts with to account id $accountId")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with to account id $accountId", e)
            throw e
        }
    }

    /**
     * Get shortcuts by bill ID
     */
    override suspend fun getShortcutsByBillId(billId: String): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getShortcutsByBillId(billId)
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts with bill id $billId")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with bill id $billId", e)
            throw e
        }
    }

    /**
     * Get shortcuts by piggybank ID
     */
    override suspend fun getShortcutsByPiggybankId(piggybankId: String): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getShortcutsByPiggybankId(piggybankId)
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts with piggybank id $piggybankId")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with piggybank id $piggybankId", e)
            throw e
        }
    }

    /**
     * Get shortcuts by category ID
     */
    override suspend fun getShortcutsByCategoryId(categoryId: String): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getShortcutsByCategoryId(categoryId)
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts with category id $categoryId")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with category id $categoryId", e)
            throw e
        }
    }

    /**
     * Get shortcuts by budget ID
     */
    override suspend fun getShortcutsByBudgetId(budgetId: String): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getShortcutsByBudgetId(budgetId)
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts with budget id $budgetId")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with budget id $budgetId", e)
            throw e
        }
    }

    /**
     * Get shortcuts by tag ID
     */
    override suspend fun getShortcutsByTagId(tagId: String): List<ShortcutEntity> {
        return try {
            val shortcuts = shortcutDao.getShortcutsByTagId(tagId)
            Log.d(TAG, "Retrieved ${shortcuts.size} shortcuts with tag id $tagId")
            shortcuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving shortcuts with tag id $tagId", e)
            throw e
        }
    }

    /**
     * Update the last used timestamp for a shortcut
     */
    override suspend fun updateShortcutLastUsed(id: Long, timestamp: Long) {
        try {
            shortcutDao.updateShortcutLastUsed(id, timestamp)
            Log.d(TAG, "Updated last used timestamp for shortcut $id to $timestamp")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating last used timestamp for shortcut $id", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalShortcutRepository"
    }
}