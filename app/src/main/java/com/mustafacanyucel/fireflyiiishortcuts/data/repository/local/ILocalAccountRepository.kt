package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

interface ILocalAccountRepository {
    suspend fun saveAccounts(accounts: List<AccountEntity>)
    suspend fun getAllAccounts(): List<AccountEntity>
    suspend fun getAccountById(id: String): AccountEntity?
    suspend fun insertAccount(account: AccountEntity): String
    suspend fun updateAccount(account: AccountEntity): Int
    suspend fun deleteAccount(id: String): Int
    fun observeAllAccounts(): Flow<List<AccountEntity>>
}