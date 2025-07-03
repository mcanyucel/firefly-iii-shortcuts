package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import kotlinx.coroutines.flow.Flow

/**
 * Simplified interface for autocut operations - no complex relationships
 * Load data as needed rather than eager loading everything
 */
interface ILocalAutocutRepository {
    // Basic CRUD operations
    suspend fun getAllAutocuts(): List<AutocutEntity>
    suspend fun getAutocutById(id: Long): AutocutEntity?
    suspend fun insertAutocut(autocut: AutocutEntity): Long
    suspend fun updateAutocut(autocut: AutocutEntity): Int
    suspend fun deleteAutocut(autocut: AutocutEntity): Int
    fun observeAllAutocuts(): Flow<List<AutocutEntity>>

    // Filter association management (just IDs)
    suspend fun getFilterIdsForAutocut(autocutId: Long): List<Long>
    suspend fun setFiltersForAutocut(autocutId: Long, filterIds: List<Long>)
    suspend fun addFilterToAutocut(autocutId: Long, filterId: Long)
    suspend fun removeFilterFromAutocut(autocutId: Long, filterId: Long)
}