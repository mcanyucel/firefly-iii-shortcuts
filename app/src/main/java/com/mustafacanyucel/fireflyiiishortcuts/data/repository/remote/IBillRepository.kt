package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.model.api.bill.BillData
import kotlinx.coroutines.flow.Flow

interface IBillRepository {
    suspend fun getBills(): Flow<ApiResult<List<BillData>>>
}