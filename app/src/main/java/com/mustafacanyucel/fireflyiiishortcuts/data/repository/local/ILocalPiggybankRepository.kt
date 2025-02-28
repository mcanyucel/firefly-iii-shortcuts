package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import kotlinx.coroutines.flow.Flow

interface ILocalPiggybankRepository {
    suspend fun savePiggybanks(piggybanks: List<PiggybankEntity>)
    suspend fun getAllPiggybanks(): List<PiggybankEntity>
    suspend fun getPiggybankById(id: String): PiggybankEntity?
    suspend fun insertPiggybank(piggybank: PiggybankEntity): String
    suspend fun updatePiggybank(piggybank: PiggybankEntity): Int
    suspend fun deletePiggybank(id: String): Int
    fun observeAllPiggybanks(): Flow<List<PiggybankEntity>>
}