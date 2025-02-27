package com.mustafacanyucel.fireflyiiishortcuts.model.api

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.services.auth.Oauth2Manager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authManager: Oauth2Manager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authState = authManager.authState.value

        // Start building a new request with the required Firefly III API headers
        val requestBuilder = originalRequest.newBuilder()
            .header("Accept", "application/vnd.api+json") // This is critical for Firefly III API

        // Add Content-Type header for requests with body (POST, PUT, etc.)
        if (originalRequest.method == "POST" || originalRequest.method == "PUT" ||
            originalRequest.method == "PATCH") {
            requestBuilder.header("Content-Type", "application/json")
        }

        // If we have a valid auth state, add the access token as a Bearer token
        if (authState != null && authState.isAuthorized) {
            Log.d("AuthInterceptor", "Adding authorization token to request")
            requestBuilder.header("Authorization", "Bearer ${authState.accessToken}")
        } else {
            Log.w("AuthInterceptor", "No valid authorization token available")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}