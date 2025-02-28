package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the piggybanks table
 */
@Dao
interface PiggybankDao {
    /**
     * Insert a single piggybank
     * @return the row ID of the inserted piggybank
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPiggybank(piggybankEntity: PiggybankEntity): Long

    /**
     * Insert multiple piggybanks
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPiggybanks(piggybankEntities: List<PiggybankEntity>)

    /**
     * Update an existing piggybank
     * @return the number of piggybanks updated (should be 1)
     */
    @Update
    suspend fun updatePiggybank(piggybankEntity: PiggybankEntity): Int

    /**
     * Get a single piggybank by ID
     */
    @Query("SELECT * FROM piggybanks WHERE id = :id")
    suspend fun getPiggybankById(id: String): PiggybankEntity?

    /**
     * Get all piggybanks
     */
    @Query("SELECT * FROM piggybanks ORDER BY name")
    suspend fun getAllPiggybanks(): List<PiggybankEntity>

    /**
     * Delete a piggybank by ID
     * @return the number of piggybanks deleted (should be 1)
     */
    @Query("DELETE FROM piggybanks WHERE id = :id")
    suspend fun deletePiggybankById(id: String): Int

    /**
     * Delete all piggybanks
     */
    @Query("DELETE FROM piggybanks")
    suspend fun deleteAllPiggybanks()

    /**
     * Observe all piggybanks as a Flow
     */
    @Query("SELECT * FROM piggybanks ORDER BY name")
    fun observeAllPiggybanks(): Flow<List<PiggybankEntity>>

    /**
     * Transaction to replace all piggybanks
     * This is useful for syncing data from the server
     */
    @Transaction
    suspend fun replaceAllPiggybanks(piggybanks: List<PiggybankEntity>) {
        deleteAllPiggybanks()
        insertPiggybanks(piggybanks)
    }
}