package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.bill.BillData
import kotlinx.coroutines.flow.Flow

interface ILocalBillRepository {
    fun getAllBills(): Flow<List<BillEntity>>
    suspend fun getBillById(id: String): BillEntity?
    suspend fun saveBills(bills: List<BillData>): Int
}