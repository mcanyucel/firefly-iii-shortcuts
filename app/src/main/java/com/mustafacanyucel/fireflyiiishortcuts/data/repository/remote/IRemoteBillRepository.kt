package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import kotlinx.coroutines.flow.Flow

interface IRemoteBillRepository {
    suspend fun getBills(): Flow<ApiResult<List<BillEntity>>>
}