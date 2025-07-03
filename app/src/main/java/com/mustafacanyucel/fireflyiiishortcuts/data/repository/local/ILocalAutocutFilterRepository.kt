package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterEntity
import kotlinx.coroutines.flow.Flow

/**
 * Simplified interface for autocut filter operations
 */
interface ILocalAutocutFilterRepository {
    // Basic CRUD operations
    suspend fun getAllAutocutFilters(): List<AutocutFilterEntity>
    suspend fun getAutocutFilterById(id: Long): AutocutFilterEntity?
    suspend fun insertAutocutFilter(filter: AutocutFilterEntity): Long
    suspend fun updateAutocutFilter(filter: AutocutFilterEntity): Int
    suspend fun deleteAutocutFilter(filter: AutocutFilterEntity): Int
    fun observeAllAutocutFilters(): Flow<List<AutocutFilterEntity>>

    // Get filters by specific criteria (for SMS matching)
    suspend fun getAutocutFiltersByTransactionType(transactionType: String): List<AutocutFilterEntity>
    suspend fun getAutocutFiltersByFromAccountMatch(match: String): List<AutocutFilterEntity>
    suspend fun getAutocutFiltersByToAccountMatch(match: String): List<AutocutFilterEntity>

    // Get filters for a specific autocut (loaded separately when needed)
    suspend fun getFiltersForAutocut(autocutId: Long): List<AutocutFilterEntity>

    // Tag association management (just IDs)
    suspend fun getTagIdsForFilter(filterId: Long): List<String>
    suspend fun setTagsForFilter(filterId: Long, tagIds: List<String>)
    suspend fun addTagToFilter(filterId: Long, tagId: String)
    suspend fun removeTagFromFilter(filterId: Long, tagId: String)
}