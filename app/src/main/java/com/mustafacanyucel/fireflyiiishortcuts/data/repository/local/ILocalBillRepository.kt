package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import kotlinx.coroutines.flow.Flow

interface ILocalBillRepository {
    suspend fun saveBills(bills: List<BillEntity>)
    suspend fun getAllBills(): List<BillEntity>
    suspend fun getBillById(id: String): BillEntity?
    suspend fun insertBill(bill: BillEntity): Long
    suspend fun updateBill(bill: BillEntity): Int
    suspend fun deleteBill(id: String): Int
    fun observeAllBills(): Flow<List<BillEntity>>
}