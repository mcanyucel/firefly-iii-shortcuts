package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank.PiggybankData
import kotlinx.coroutines.flow.Flow

interface ILocalPiggybankRepository {
    fun getAllPiggybanks(): Flow<List<PiggybankEntity>>
    suspend fun getPiggybankById(id: String): PiggybankEntity?
    suspend fun savePiggybanks(piggybanks: List<PiggybankData>): Int
}