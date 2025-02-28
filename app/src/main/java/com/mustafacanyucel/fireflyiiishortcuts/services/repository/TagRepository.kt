package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.model.api.TagData
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
class TagRepository @Inject constructor(
    private val apiService: FireflyIiiApiService
) : ITagRepository {
    override suspend fun getTags(): Flow<ApiResult<List<TagData>>> = flow {
        try {
            val api = apiService.getApi()

            val firstPage = api.getTags(page = 1)
            val totalPages = firstPage.meta.pagination.totalPages

            val allTags = firstPage.data.toMutableList()

            if (totalPages > 1) {
                for (page in 2..totalPages) {
                    try {
                        val nextPage = api.getTags(page = page)
                        allTags.addAll(nextPage.data)
                    } catch (e: Exception) {
                        Log.e("TagRepository", "Error fetching page $page: ${e.message}", e)
                        emit(ApiResult.Error("Error fetching page $page: ${e.message}. Continuing with partial results."))
                        break
                    }

                }
            }

            Log.d("TagRepository", "Total tags fetched: ${allTags.size}")
            emit(ApiResult.Success(allTags))
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