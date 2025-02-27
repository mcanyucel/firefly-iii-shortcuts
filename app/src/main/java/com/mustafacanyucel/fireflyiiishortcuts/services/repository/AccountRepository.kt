package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.model.api.AccountData
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
class AccountRepository @Inject constructor(
    private val apiService: FireflyIiiApiService
) : IAccountRepository {

    override suspend fun getAccounts(type: String?, date: String?): Flow<ApiResult<List<AccountData>>> = flow {
        try {
            val api = apiService.getApi()
            Log.d("AccountRepository", "Fetching accounts with type: $type, date: $date")

            // Fetch first page to get pagination info
            val firstPage = api.getAccounts(page = 1, type = type, date = date)
            val totalPages = firstPage.meta.pagination.totalPages
            val total = firstPage.meta.pagination.total

            Log.d("AccountRepository", "First page fetched. Total accounts: $total, Total pages: $totalPages")

            // Start with first page results
            val allAccounts = firstPage.data.toMutableList()

            // Important: Only emit after fetching ALL pages
            if (totalPages > 1) {
                Log.d("AccountRepository", "Need to fetch ${totalPages - 1} more pages")

                // Fetch remaining pages if needed
                for (page in 2..totalPages) {
                    try {
                        Log.d("AccountRepository", "Fetching page $page of $totalPages")
                        val nextPage = api.getAccounts(page = page, type = type, date = date)
                        allAccounts.addAll(nextPage.data)
                        Log.d("AccountRepository", "Page $page fetched. Running total: ${allAccounts.size}/${total}")
                    } catch (e: Exception) {
                        Log.e("AccountRepository", "Error fetching page $page: ${e.message}", e)
                        // We'll continue with partial results rather than failing completely
                        emit(ApiResult.Error("Error fetching page $page: ${e.message}. Continuing with partial results."))
                        break
                    }
                }
            }

            Log.d("AccountRepository", "All pages fetched. Total accounts: ${allAccounts.size}")
            // Emit success with ALL accounts after all pages are fetched
            emit(ApiResult.Success(allAccounts))

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

    override suspend fun testConnection(): ApiResult<Boolean> {
        return try {
            val result = apiService.testConnection()
            ApiResult.Success(result)
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is HttpException -> {
                    val code = e.code()
                    val errorBody = e.response()?.errorBody()?.string()
                    "HTTP Error $code: ${getHttpErrorMessage(code, errorBody)}"
                }
                is UnknownHostException -> "Cannot connect to server. Please check the URL and your internet connection."
                is SocketTimeoutException -> "Connection timed out. The server took too long to respond."
                is IOException -> "Network error: ${e.message}"
                else -> "Unexpected error: ${e.message}"
            }
            val errorCode = if (e is HttpException) e.code() else null
            ApiResult.Error(errorMessage, errorCode, e)
        }
    }

    private fun getHttpErrorMessage(code: Int, errorBody: String?): String {
        return when (code) {
            400 -> "Bad request. The request couldn't be understood."
            401 -> "Unauthorized. Please check your authorization settings."
            403 -> "Forbidden. You don't have permission to access this resource."
            404 -> "Not found. The requested endpoint doesn't exist. Please check your server URL."
            500, 502, 503, 504 -> "Server error. Please try again later."
            else -> errorBody ?: "Unknown error"
        }
    }
}