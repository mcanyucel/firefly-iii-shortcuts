package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank.PiggybankData
import kotlinx.coroutines.flow.Flow

interface IRemotePiggybankRepository {
    suspend fun getPiggybanks(): Flow<ApiResult<List<PiggybankEntity>>>
}