package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.AccountDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.account.AccountData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAccountRepository @Inject constructor(
    private val accountDao: AccountDao
) : ILocalAccountRepository {
    override fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

    override fun getAccountsByType(type: String): Flow<List<AccountEntity>> {
        return accountDao.getAccountsByType(type)
    }

    override suspend fun getAccountById(id: String): AccountEntity? {
        return accountDao.getAccountById(id)
    }

    override suspend fun saveAccounts(accounts: List<AccountData>): Int {
        try {
            Log.d(TAG, "Saving ${accounts.size} accounts to the database")
            val entities = accounts.map { AccountEntity.fromApiModel(it) }

            accountDao.replaceAllAccounts(entities)
            Log.d(TAG, "Successfully saved ${entities.size} accounts to the database")
            return entities.size
        } catch (e: Exception) {
            Log.e(TAG, "Error saving accounts to the database", e)
            throw e
        }
    }

    override suspend fun getLastSyncTimestamp(): Long? {
        return accountDao.getLastUpdateTimestamp()
    }

    override suspend fun getAccountCount(): Int {
        return accountDao.getAccountCount()
    }

    companion object {
        private const val TAG = "LocalAccountRepository"
    }
}