package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import com.mustafacanyucel.fireflyiiishortcuts.model.api.bill.BillData
import kotlinx.coroutines.flow.Flow

interface IBillRepository {
    suspend fun getBills(): Flow<ApiResult<List<BillData>>>
}