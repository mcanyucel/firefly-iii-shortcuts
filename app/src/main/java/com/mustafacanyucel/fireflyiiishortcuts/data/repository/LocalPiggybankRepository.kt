package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.PiggybankDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank.PiggybankData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPiggybankRepository @Inject constructor(
    private val piggybankDao: PiggybankDao
): ILocalPiggybankRepository {
    override fun getAllPiggybanks(): Flow<List<PiggybankEntity>> {
        return piggybankDao.getAllPiggybanks()
    }

    override suspend fun getPiggybankById(id: String): PiggybankEntity? {
        return piggybankDao.getPiggybankById(id)
    }

    override suspend fun savePiggybanks(piggybanks: List<PiggybankData>): Int {
        try {
            Log.d(TAG, "Saving #{piggybanks.size} piggybanks to database")
            val entities = piggybanks.map { PiggybankEntity.fromApiModel(it) }
            piggybankDao.replaceAllPiggybanks(entities)
            Log.d(TAG, "Successfully saved #{piggybanks.size} piggybanks to database")
            return entities.size
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save piggybanks to database", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalPiggybankRepository"
    }
}