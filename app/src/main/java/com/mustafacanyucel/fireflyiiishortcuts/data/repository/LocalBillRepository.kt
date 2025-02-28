package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BillDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.bill.BillData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBillRepository @Inject constructor(
    private val billDao: BillDao
) : ILocalBillRepository {
    override fun getAllBills(): Flow<List<BillEntity>> {
        return billDao.getBills()
    }

    override suspend fun getBillById(id: String): BillEntity? {
        return billDao.getBillById(id)
    }

    override suspend fun saveBills(bills: List<BillData>): Int {
        try {
            Log.d(TAG, "Saving ${bills.size} bills to the database")
            val entities = bills.map { BillEntity.fromApiModel(it) }

            billDao.replaceAllBills(entities)
            Log.d(TAG, "Successfully saved ${bills.size} bills to the database")
            return bills.size
        } catch (e: Exception) {
            Log.e(TAG, "Error saving bills to the database", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalBillRepository"
    }
}