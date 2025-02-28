package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank.PiggybankData
import kotlinx.coroutines.flow.Flow

interface IPiggybankRepository {
    suspend fun getPiggybanks(): Flow<ApiResult<List<PiggybankData>>>
}