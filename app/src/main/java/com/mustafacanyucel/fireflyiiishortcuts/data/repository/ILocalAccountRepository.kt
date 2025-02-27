package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.AccountData
import kotlinx.coroutines.flow.Flow

interface ILocalAccountRepository {
    fun getAllAccounts(): Flow<List<AccountEntity>>
    fun getAccountsByType(type: String): Flow<List<AccountEntity>>
    suspend fun getAccountById(id: String): AccountEntity?
    suspend fun saveAccounts(accounts: List<AccountData>): Int
    suspend fun getLastSyncTimestamp(): Long?
    suspend fun getAccountCount(): Int
}