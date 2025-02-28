package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the bills table
 */
@Dao
interface BillDao {
    /**
     * Insert a single bill
     * @return the row ID of the inserted bill
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(billEntity: BillEntity): Long

    /**
     * Insert multiple bills
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(billEntities: List<BillEntity>)

    /**
     * Update an existing bill
     * @return the number of bills updated (should be 1)
     */
    @Update
    suspend fun updateBill(billEntity: BillEntity): Int

    /**
     * Get a single bill by ID
     */
    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getBillById(id: String): BillEntity?

    /**
     * Get all bills
     */
    @Query("SELECT * FROM bills ORDER BY name")
    suspend fun getAllBills(): List<BillEntity>

    /**
     * Delete a bill by ID
     * @return the number of bills deleted (should be 1)
     */
    @Query("DELETE FROM bills WHERE id = :id")
    suspend fun deleteBillById(id: String): Int

    /**
     * Delete all bills
     */
    @Query("DELETE FROM bills")
    suspend fun deleteAllBills()

    /**
     * Observe all bills as a Flow
     */
    @Query("SELECT * FROM bills ORDER BY name")
    fun observeAllBills(): Flow<List<BillEntity>>


    /**
     * Transaction to replace all bills
     * This is useful for syncing data from the server
     */
    @Transaction
    suspend fun replaceAllBills(bills: List<BillEntity>) {
        deleteAllBills()
        insertBills(bills)
    }
}