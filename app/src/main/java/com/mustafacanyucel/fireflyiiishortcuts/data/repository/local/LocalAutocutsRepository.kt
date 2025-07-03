package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.AutocutDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAutocutsRepository @Inject constructor(
    private val autocutDao: AutocutDao
) : ILocalAutocutRepository {
    override suspend fun saveAutocuts(autocuts: List<AutocutEntity>) {
        try {
            autocutDao.insertAutocuts(autocuts)
            Log.d(TAG, "Saved ${autocuts.size} autocuts to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving autocuts: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAllAutocuts(): List<AutocutEntity> {
        return try {
            val autocuts = autocutDao.getAllAutocuts()
            Log.d(TAG, "Retrieved ${autocuts.size} autocuts from database")
            autocuts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving autocuts: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAutocutById(id: Long): AutocutEntity? {
        return try {
            val autocut = autocutDao.getAutocutById(id)
            Log.d(TAG, "Retrieved autocut with ID $id from database")
            autocut
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving autocut by ID $id: ${e.message}", e)
            throw e
        }
    }

    override suspend fun insertAutocut(autocut: AutocutEntity): Long {
        return try {
            val id = autocutDao.insertAutocut(autocut)
            Log.d(TAG, "Inserted autocut with ID $id into database")
            id
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting autocut: ${e.message}", e)
            throw e
        }
    }

    override suspend fun updateAutocut(autocut: AutocutEntity): Int {
        return try {
            val rowsUpdated = autocutDao.updateAutocut(autocut)
            Log.d(TAG, "Updated autocut with ID ${autocut.id} in database")
            rowsUpdated
        } catch (e: Exception) {
            Log.e(TAG, "Error updating autocut: ${e.message}", e)
            throw e
        }
    }

    override suspend fun deleteAutocut(autocut: AutocutEntity): Int {
        return try {
            val rowsDeleted = autocutDao.deleteAutocut(autocut)
            Log.d(TAG, "Deleted autocut with ID ${autocut.id} from database")
            rowsDeleted
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting autocut with ID ${autocut.id}: ${e.message}", e)
            throw e
        }
    }

    override fun observeAllAutocuts(): Flow<List<AutocutEntity>> {
        return try {
            autocutDao.observeAllAutocuts()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing all autocuts: ${e.message}", e)
            throw e
        }
    }

    override suspend fun saveAutocutWithFilter(
        autocut: AutocutEntity,
        filterIds: List<String>
    ): Long {
        throw NotImplementedError("Method not implemented yet")
    }

    override suspend fun deleteAutocutFiltersForAutocut(autocutId: Long) {
        throw NotImplementedError("Method not implemented yet")
    }

    override suspend fun getAutocutWithAutocutFilters(autocutId: Long): AutocutEntity? {
        throw NotImplementedError("Method not implemented yet")
    }

    override suspend fun getAllAutocutsWithAutocutFilters(): List<AutocutEntity> {
        throw NotImplementedError("Method not implemented yet")
    }

    companion object {
        private const val TAG = "LocalAutocutsRepository"
    }
}