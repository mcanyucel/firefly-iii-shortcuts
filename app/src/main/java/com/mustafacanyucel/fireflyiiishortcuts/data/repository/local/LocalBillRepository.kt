package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BillDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBillRepository @Inject constructor(
    private val billDao: BillDao
) : ILocalBillRepository {

    /**
     * Save a list of bills to the database.
     * This implementation uses a transaction to replace all bills at once.
     */
    override suspend fun saveBills(bills: List<BillEntity>) {
        try {
            // Use a transaction to ensure data consistency
            billDao.replaceAllBills(bills)
            Log.d(TAG, "Saved ${bills.size} bills to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving bills to database", e)
            throw e
        }
    }

    /**
     * Get all bills from the database
     */
    override suspend fun getAllBills(): List<BillEntity> {
        return try {
            val bills = billDao.getAllBills()
            Log.d(TAG, "Retrieved ${bills.size} bills from database")
            bills
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving bills from database", e)
            throw e
        }
    }

    /**
     * Get a bill by its ID
     */
    override suspend fun getBillById(id: String): BillEntity? {
        return try {
            val bill = billDao.getBillById(id)
            if (bill != null) {
                Log.d(TAG, "Retrieved bill with id $id")
            } else {
                Log.d(TAG, "No bill found with id $id")
            }
            bill
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving bill with id $id", e)
            throw e
        }
    }

    /**
     * Insert a new bill and return its ID
     */
    override suspend fun insertBill(bill: BillEntity): Long {
        return try {
            val insertedId = billDao.insertBill(bill)
            Log.d(TAG, "Inserted bill with id $insertedId")
            insertedId
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting bill", e)
            throw e
        }
    }

    /**
     * Update an existing bill
     * @return Number of rows updated (should be 1 if successful)
     */
    override suspend fun updateBill(bill: BillEntity): Int {
        return try {
            val updatedRows = billDao.updateBill(bill)
            Log.d(TAG, "Updated $updatedRows bill(s) with id ${bill.id}")
            updatedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error updating bill with id ${bill.id}", e)
            throw e
        }
    }

    /**
     * Delete a bill by its ID
     * @return Number of rows deleted (should be 1 if successful)
     */
    override suspend fun deleteBill(id: String): Int {
        return try {
            val deletedRows = billDao.deleteBillById(id)
            Log.d(TAG, "Deleted $deletedRows bill(s) with id $id")
            deletedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting bill with id $id", e)
            throw e
        }
    }

    /**
     * Observe all bills and emit updates when the data changes
     */
    override fun observeAllBills(): Flow<List<BillEntity>> {
        return try {
            billDao.observeAllBills()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing bills", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalBillRepository"
    }
}