package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.ApiHelper.Companion.getHttpErrorMessage
import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionRequest
import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionResponse
import com.mustafacanyucel.fireflyiiishortcuts.services.firefly.FireflyIiiApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteTransactionRepository @Inject constructor(
    private val apiService: FireflyIiiApiService
): IRemoteTransactionRepository{
    override suspend fun createTransaction(transactionRequest: TransactionRequest): Flow<ApiResult<TransactionResponse>> = flow {
        try {
            val api = apiService.getApi()
            Log.d(TAG, "Creating transaction: $transactionRequest")
            val response = api.createTransaction(transactionRequest)
            Log.d(TAG, "Transaction created: $response")
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is HttpException -> {
                    val code = e.code()
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e(TAG, "HTTP Error $code: $errorBody", e)
                    "Server error (HTTP $code): ${getHttpErrorMessage(code, errorBody)}"
                }
                is UnknownHostException -> {
                    Log.e("TransactionRepository", "Unknown host: ${e.message}", e)
                    "Cannot connect to server. Please check your internet connection and server URL."
                }
                is SocketTimeoutException -> {
                    Log.e("TransactionRepository", "Connection timeout: ${e.message}", e)
                    "Connection timed out. The server took too long to respond."
                }
                is IOException -> {
                    Log.e("TransactionRepository", "Network error: ${e.message}", e)
                    "Network error: ${e.message ?: "Unknown IO error"}"
                }
                else -> {
                    Log.e("TransactionRepository", "Unexpected error: ${e.message}", e)
                    "Unexpected error: ${e.message ?: "Unknown error"}"
                }
            }

            val errorCode = if (e is HttpException) e.code() else null
            emit(ApiResult.Error(errorMessage, errorCode, exception = e))
        }
    }

    companion object {
        private const val TAG = "RemoteTransactionRepository"
    }
}