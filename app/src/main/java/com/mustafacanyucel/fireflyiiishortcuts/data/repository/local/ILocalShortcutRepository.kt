package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutWithTags
import kotlinx.coroutines.flow.Flow

interface ILocalShortcutRepository {
    suspend fun saveShortcuts(shortcuts: List<ShortcutEntity>)
    suspend fun getAllShortcuts(): List<ShortcutEntity>
    suspend fun getShortcutById(id: Long): ShortcutEntity?
    suspend fun insertShortcut(shortcut: ShortcutEntity): Long
    suspend fun updateShortcut(shortcut: ShortcutEntity): Int
    suspend fun deleteShortcut(id: Long): Int
    fun observeAllShortcuts(): Flow<List<ShortcutEntity>>

    // Tag relationship methods
    suspend fun saveShortcutWithTags(shortcut: ShortcutEntity, tagIds: List<String>): Long
    suspend fun getShortcutWithTags(shortcutId: Long): ShortcutWithTags?
    suspend fun getAllShortcutsWithTags(): List<ShortcutWithTags>
    fun observeAllShortcutsWithTags(): Flow<List<ShortcutWithTags>>

    // Relationship-based queries
    suspend fun getShortcutsByFromAccountId(accountId: String): List<ShortcutEntity>
    suspend fun getShortcutsByToAccountId(accountId: String): List<ShortcutEntity>
    suspend fun getShortcutsByCategoryId(categoryId: String): List<ShortcutEntity>
    suspend fun getShortcutsByBudgetId(budgetId: String): List<ShortcutEntity>
    suspend fun getShortcutsByTagId(tagId: String): List<ShortcutEntity>
    suspend fun getShortcutsByBillId(billId: String): List<ShortcutEntity>
    suspend fun getShortcutsByPiggybankId(piggybankId: String): List<ShortcutEntity>

    // Utility method
    suspend fun updateShortcutLastUsed(id: Long, timestamp: Long = System.currentTimeMillis())
}