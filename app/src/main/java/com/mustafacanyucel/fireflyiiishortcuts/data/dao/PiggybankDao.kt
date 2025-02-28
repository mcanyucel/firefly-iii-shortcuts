package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PiggybankDao {
    /**
     * Get all piggybanks as a flow
     */
    @Query("SELECT * FROM piggybanks ORDER BY name ASC")
    fun getAllPiggybanks(): Flow<List<PiggybankEntity>>

    /**
     * Get a specific piggybank by id
     */
    @Query("SELECT * FROM piggybanks WHERE id = :id")
    suspend fun getPiggybankById(id: String): PiggybankEntity?

    /**
     * Insert a single piggybank
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPiggybank(piggybank: PiggybankEntity)

    /**
     * Insert multiple piggybanks
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPiggybanks(piggybanks: List<PiggybankEntity>)

    /**
     * Delete all piggybanks
     */
    @Query("DELETE FROM piggybanks")
    suspend fun deleteAllPiggybanks()

    /**
     * Replace all piggybanks in a single transaction
     */
    @Transaction
    suspend fun replaceAllPiggybanks(piggybanks: List<PiggybankEntity>) {
        deleteAllPiggybanks()
        insertPiggybanks(piggybanks)
    }

}