package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.PiggybankDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPiggybankRepository @Inject constructor(
    private val piggybankDao: PiggybankDao
) : ILocalPiggybankRepository {

    /**
     * Save a list of piggybanks to the database.
     * This implementation uses a transaction to replace all piggybanks at once.
     */
    override suspend fun savePiggybanks(piggybanks: List<PiggybankEntity>) {
        try {
            // Use a transaction to ensure data consistency
            piggybankDao.replaceAllPiggybanks(piggybanks)
            Log.d(TAG, "Saved ${piggybanks.size} piggybanks to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving piggybanks to database", e)
            throw e
        }
    }

    /**
     * Get all piggybanks from the database
     */
    override suspend fun getAllPiggybanks(): List<PiggybankEntity> {
        return try {
            val piggybanks = piggybankDao.getAllPiggybanks()
            Log.d(TAG, "Retrieved ${piggybanks.size} piggybanks from database")
            piggybanks
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving piggybanks from database", e)
            throw e
        }
    }

    /**
     * Get a piggybank by its ID
     */
    override suspend fun getPiggybankById(id: String): PiggybankEntity? {
        return try {
            val piggybank = piggybankDao.getPiggybankById(id)
            if (piggybank != null) {
                Log.d(TAG, "Retrieved piggybank with id $id")
            } else {
                Log.d(TAG, "No piggybank found with id $id")
            }
            piggybank
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving piggybank with id $id", e)
            throw e
        }
    }

    /**
     * Insert a new piggybank and return its ID
     * Note: Since the interface specifies returning a String ID rather than the Long row ID
     * that Room provides, we'll return the piggybank's ID directly
     */
    override suspend fun insertPiggybank(piggybank: PiggybankEntity): String {
        return try {
            // Insert and ignore the returned row ID
            piggybankDao.insertPiggybank(piggybank)
            Log.d(TAG, "Inserted piggybank with id ${piggybank.id}")
            piggybank.id
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting piggybank with id ${piggybank.id}", e)
            throw e
        }
    }

    /**
     * Update an existing piggybank
     * @return Number of rows updated (should be 1 if successful)
     */
    override suspend fun updatePiggybank(piggybank: PiggybankEntity): Int {
        return try {
            val updatedRows = piggybankDao.updatePiggybank(piggybank)
            Log.d(TAG, "Updated $updatedRows piggybank(s) with id ${piggybank.id}")
            updatedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error updating piggybank with id ${piggybank.id}", e)
            throw e
        }
    }

    /**
     * Delete a piggybank by its ID
     * @return Number of rows deleted (should be 1 if successful)
     */
    override suspend fun deletePiggybank(id: String): Int {
        return try {
            val deletedRows = piggybankDao.deletePiggybankById(id)
            Log.d(TAG, "Deleted $deletedRows piggybank(s) with id $id")
            deletedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting piggybank with id $id", e)
            throw e
        }
    }

    /**
     * Observe all piggybanks and emit updates when the data changes
     */
    override fun observeAllPiggybanks(): Flow<List<PiggybankEntity>> {
        return try {
            piggybankDao.observeAllPiggybanks()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing piggybanks", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalPiggybankRepository"
    }
}