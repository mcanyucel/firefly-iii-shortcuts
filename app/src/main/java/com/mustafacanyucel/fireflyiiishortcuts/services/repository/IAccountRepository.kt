package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import com.mustafacanyucel.fireflyiiishortcuts.model.api.account.AccountData
import kotlinx.coroutines.flow.Flow

interface IAccountRepository {
    suspend fun getAccounts(type: String? = null, date: String? = null): Flow<ApiResult<List<AccountData>>>
    suspend fun testConnection(): ApiResult<Boolean>
}