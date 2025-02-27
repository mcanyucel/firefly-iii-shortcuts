package com.mustafacanyucel.fireflyiiishortcuts.services.api

import com.mustafacanyucel.fireflyiiishortcuts.utils.DebugUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionDebugInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Use our debug utility to log request details
        DebugUtils.logRequest(request)

        try {
            val response = chain.proceed(request)

            // Use our debug utility to log response details
            DebugUtils.logResponse(response)

            // If we got a 404, do deeper analysis
            if (response.code == 404) {
                val url = request.url.toString()
                val baseUrl = url.substring(0, url.indexOf("/", 8)) // Skip http:// or https://
                val endpoint = url.substring(baseUrl.length)

                DebugUtils.analyze404Error(baseUrl, endpoint)
            }

            return response
        } catch (e: Exception) {
            DebugUtils.logApiError(e, "connection")
            throw e
        }
    }
}