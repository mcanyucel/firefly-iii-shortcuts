package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
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
class RemoteAccountRepository @Inject constructor(
    private val apiService: FireflyIiiApiService
) : IRemoteAccountRepository {

    override suspend fun getAccounts(
        type: String?,
        date: String?
    ): Flow<ApiResult<List<AccountEntity>>> = flow {
        try {
            val api = apiService.getApi()
            Log.d("AccountRepository", "Fetching accounts with type: $type, date: $date")

            // Fetch first page to get pagination info
            val firstPage = api.getAccounts(page = 1, type = type, date = date)
            val totalPages = firstPage.meta.pagination.totalPages
            val total = firstPage.meta.pagination.total

            Log.d(
                "AccountRepository",
                "First page fetched. Total accounts: $total, Total pages: $totalPages"
            )

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
                        Log.d(
                            "AccountRepository",
                            "Page $page fetched. Running total: ${allAccounts.size}/${total}"
                        )
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
            emit(ApiResult.Success(allAccounts.map { AccountEntity.fromApiModel(it) }))

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