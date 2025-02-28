package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

interface IRemoteAccountRepository {
    suspend fun getAccounts(type: String? = null, date: String? = null): Flow<ApiResult<List<AccountEntity>>>
}