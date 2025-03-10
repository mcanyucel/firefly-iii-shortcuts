package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionRequest
import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionResponse
import kotlinx.coroutines.flow.Flow

interface IRemoteTransactionRepository {
    suspend fun createTransaction(transactionRequest: TransactionRequest): Flow<ApiResult<TransactionResponse>>
}