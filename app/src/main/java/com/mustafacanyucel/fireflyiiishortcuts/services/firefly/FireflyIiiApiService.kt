package com.mustafacanyucel.fireflyiiishortcuts.services.firefly

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.model.api.FireflyIiiApi
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireflyIiiApiService @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val preferencesRepository: IPreferencesRepository,
) {
    private var currentServerUrl: String? = null
    private var retrofitApi: FireflyIiiApi? = null

    suspend fun getApi(): FireflyIiiApi {
        // Get base URL and clean it
        var serverUrl = preferencesRepository.getString(preferencesRepository.serverUrlKey, "")

        // Clean up server URL to avoid double slashes
        serverUrl = serverUrl.trim()
        if (serverUrl.endsWith("/")) {
            serverUrl = serverUrl.substring(0, serverUrl.length - 1)
        }

        if (serverUrl.isEmpty()) {
            Log.e("FireflyAPI", "Server URL is empty!")
            throw IllegalStateException("Server URL is not set")
        }

        // Properly construct the API base URL
        val apiBaseUrl = "$serverUrl/api"

        // Log the server URL for debugging
        Log.d("FireflyAPI", "Using server URL: $serverUrl")
        Log.d("FireflyAPI", "Using API base URL: $apiBaseUrl")

        try {
            // Parse and validate the URL to check for issues
            val httpUrl = apiBaseUrl.toHttpUrlOrNull()
                ?: throw IllegalArgumentException("Invalid URL: $apiBaseUrl")

            // Log the parsed URL components
            Log.d(
                "FireflyAPI", "URL validated: " +
                        "Scheme=${httpUrl.scheme}, " +
                        "Host=${httpUrl.host}, " +
                        "Path=${httpUrl.encodedPath}"
            )

        } catch (e: Exception) {
            Log.e("FireflyAPI", "Invalid server URL format: ${e.message}", e)
            throw IllegalArgumentException("Invalid server URL format: ${e.message}", e)
        }

        // Check if we need to create a new Retrofit instance
        if (apiBaseUrl != currentServerUrl || retrofitApi == null) {
            try {
                // Make sure the base URL ends with a slash for Retrofit
                val baseUrlForRetrofit =
                    if (apiBaseUrl.endsWith("/")) apiBaseUrl else "$apiBaseUrl/"

                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrlForRetrofit)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                retrofitApi = retrofit.create(FireflyIiiApi::class.java)
                currentServerUrl = apiBaseUrl

                Log.d(
                    "FireflyAPI",
                    "Created new Retrofit instance with baseUrl: $baseUrlForRetrofit"
                )
            } catch (e: Exception) {
                Log.e("FireflyAPI", "Failed to create Retrofit instance: ${e.message}", e)
                throw e
            }
        }

        return retrofitApi!!
    }

    // A helper method to test the connection to the server
    suspend fun testConnection(): Boolean {
        return try {
            val api = getApi()
            // We'll just fetch the first page with a limit of 1 to minimize data transfer
            val response = api.getAccounts(limit = 1)
            Log.d(
                "FireflyAPI",
                "Test connection successful. Found ${response.meta.pagination.total} accounts"
            )
            true
        } catch (e: Exception) {
            Log.e("FireflyAPI", "Test connection failed: ${e.message}", e)
            false
        }
    }
}