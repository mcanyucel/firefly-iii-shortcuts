package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.ApiHelper.Companion.getHttpErrorMessage
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
class RemotePiggybankRepository @Inject constructor(
    private val apiService: FireflyIiiApiService
) : IRemotePiggybankRepository {
    override suspend fun getPiggybanks(): Flow<ApiResult<List<PiggybankEntity>>> = flow {
        try {
            val api = apiService.getApi()

            val firstPage = api.getPiggyBanks(page = 1)
            val totalPages = firstPage.meta.pagination.totalPages

            val allPiggybanks = firstPage.data.toMutableList()

            if (totalPages > 1) {
                for (page in 2..totalPages) {
                    try {
                        val nextPage = api.getPiggyBanks(page = page)
                        allPiggybanks.addAll(nextPage.data)
                    } catch (e: Exception) {
                        Log.e("PiggybankRepository", "Error fetching page $page: ${e.message}", e)
                        emit(ApiResult.Error("Error fetching page $page: ${e.message}. Continuing with partial results."))
                        break
                    }
                }
            }

            Log.d("PiggybankRepository", "Total piggybanks fetched: ${allPiggybanks.size}")
            emit(ApiResult.Success(allPiggybanks.map { PiggybankEntity.fromApiModel(it) }))
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is HttpException -> {
                    val code = e.code()
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("AccountRepository", "HTTP Error $code: $errorBody", e)
                    "Server error (HTTP $code): ${getHttpErrorMessage(code, errorBody)}"
                }

                is UnknownHostException -> {
                    Log.e("AccountRepository", "Unknown host: ${e.message}", e)
                    "Cannot connect to server. Please check your internet connection and server URL."
                }

                is SocketTimeoutException -> {
                    Log.e("AccountRepository", "Connection timeout: ${e.message}", e)
                    "Connection timed out. The server took too long to respond."
                }

                is IOException -> {
                    Log.e("AccountRepository", "Network error: ${e.message}", e)
                    "Network error: ${e.message ?: "Unknown IO error"}"
                }

                else -> {
                    Log.e("AccountRepository", "Unexpected error: ${e.message}", e)
                    "Unexpected error: ${e.message ?: "Unknown error"}"
                }
            }

            val errorCode = if (e is HttpException) e.code() else null
            emit(ApiResult.Error(errorMessage, errorCode, e))
        }
    }
}