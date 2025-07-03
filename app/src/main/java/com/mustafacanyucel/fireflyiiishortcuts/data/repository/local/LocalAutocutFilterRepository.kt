package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.AutocutFilterDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterTagCrossRef
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAutocutFilterRepository @Inject constructor(
    private val autocutFilterDao: AutocutFilterDao
) : ILocalAutocutFilterRepository {

    override suspend fun getAllAutocutFilters(): List<AutocutFilterEntity> {
        return try {
            val filters = autocutFilterDao.getAllAutocutFilters()
            Log.d(TAG, "Retrieved ${filters.size} autocut filters from database")
            filters
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving autocut filters: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAutocutFilterById(id: Long): AutocutFilterEntity? {
        return try {
            val filter = autocutFilterDao.getAutocutFilterById(id)
            Log.d(TAG, "Retrieved autocut filter with ID $id from database")
            filter
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving autocut filter by ID $id: ${e.message}", e)
            throw e
        }
    }

    override suspend fun insertAutocutFilter(filter: AutocutFilterEntity): Long {
        return try {
            val id = autocutFilterDao.insertAutocutFilter(filter)
            Log.d(TAG, "Inserted autocut filter with ID $id into database")
            id
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting autocut filter: ${e.message}", e)
            throw e
        }
    }

    override suspend fun updateAutocutFilter(filter: AutocutFilterEntity): Int {
        return try {
            val rowsUpdated = autocutFilterDao.updateAutocutFilter(filter)
            Log.d(TAG, "Updated autocut filter with ID ${filter.id} in database")
            rowsUpdated
        } catch (e: Exception) {
            Log.e(TAG, "Error updating autocut filter: ${e.message}", e)
            throw e
        }
    }

    override suspend fun deleteAutocutFilter(filter: AutocutFilterEntity): Int {
        return try {
            val rowsDeleted = autocutFilterDao.deleteAutocutFilter(filter)
            Log.d(TAG, "Deleted autocut filter with ID ${filter.id} from database")
            rowsDeleted
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting autocut filter with ID ${filter.id}: ${e.message}", e)
            throw e
        }
    }

    override fun observeAllAutocutFilters(): Flow<List<AutocutFilterEntity>> {
        return try {
            autocutFilterDao.observeAllAutocutFilters()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing all autocut filters: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAutocutFiltersByTransactionType(transactionType: String): List<AutocutFilterEntity> {
        return try {
            val filters = autocutFilterDao.getAutocutFiltersByTransactionType(transactionType)
            Log.d(TAG, "Retrieved ${filters.size} autocut filters for transaction type $transactionType")
            filters
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving autocut filters by transaction type: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAutocutFiltersByFromAccountMatch(match: String): List<AutocutFilterEntity> {
        return try {
            val filters = autocutFilterDao.getAutocutFiltersByFromAccountMatch(match)
            Log.d(TAG, "Retrieved ${filters.size} autocut filters for from account match: $match")
            filters
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving autocut filters by from account match: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAutocutFiltersByToAccountMatch(match: String): List<AutocutFilterEntity> {
        return try {
            val filters = autocutFilterDao.getAutocutFiltersByToAccountMatch(match)
            Log.d(TAG, "Retrieved ${filters.size} autocut filters for to account match: $match")
            filters
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving autocut filters by to account match: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getFiltersForAutocut(autocutId: Long): List<AutocutFilterEntity> {
        return try {
            val filters = autocutFilterDao.getFiltersForAutocut(autocutId)
            Log.d(TAG, "Retrieved ${filters.size} filters for autocut ID $autocutId")
            filters
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving filters for autocut ID $autocutId: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getTagIdsForFilter(filterId: Long): List<String> {
        return try {
            val tagIds = autocutFilterDao.getTagIdsForFilter(filterId)
            Log.d(TAG, "Retrieved ${tagIds.size} tag IDs for filter $filterId")
            tagIds
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving tag IDs for filter $filterId: ${e.message}", e)
            throw e
        }
    }

    override suspend fun setTagsForFilter(filterId: Long, tagIds: List<String>) {
        try {
            autocutFilterDao.setTagsForFilter(filterId, tagIds)
            Log.d(TAG, "Set ${tagIds.size} tags for filter $filterId")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting tags for filter $filterId: ${e.message}", e)
            throw e
        }
    }

    override suspend fun addTagToFilter(filterId: Long, tagId: String) {
        try {
            val crossRef = AutocutFilterTagCrossRef(filterId, tagId)
            autocutFilterDao.insertFilterTagCrossRef(crossRef)
            Log.d(TAG, "Added tag $tagId to filter $filterId")
        } catch (e: Exception) {
            Log.e(TAG, "Error adding tag $tagId to filter $filterId: ${e.message}", e)
            throw e
        }
    }

    override suspend fun removeTagFromFilter(filterId: Long, tagId: String) {
        try {
            val crossRef = AutocutFilterTagCrossRef(filterId, tagId)
            autocutFilterDao.deleteFilterTagCrossRef(crossRef)
            Log.d(TAG, "Removed tag $tagId from filter $filterId")
        } catch (e: Exception) {
            Log.e(TAG, "Error removing tag $tagId from filter $filterId: ${e.message}", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalAutocutFilterRepository"
    }
}