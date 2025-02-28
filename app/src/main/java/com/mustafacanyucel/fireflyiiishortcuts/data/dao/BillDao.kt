package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    /**
     * Get all bills as a flow
     */
    @Query("SELECT * FROM bills ORDER BY name ASC")
    fun getBills(): Flow<List<BillEntity>>

    /**
     * Get a single bill by id
     */
    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getBillById(id: String): BillEntity?

    /**
     * Insert a single bill
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity)

    /**
     * Insert multiple bills
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<BillEntity>)

    /**
     * Delete all bills
     */
    @Query("DELETE FROM bills")
    suspend fun deleteAllBills()

    /**
     * Replace all bills in a single transaction
     */
    @Transaction
    suspend fun replaceAllBills(bills: List<BillEntity>) {
        deleteAllBills()
        insertBills(bills)
    }


}