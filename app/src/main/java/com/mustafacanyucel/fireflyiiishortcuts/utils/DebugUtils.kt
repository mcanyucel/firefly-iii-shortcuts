package com.mustafacanyucel.fireflyiiishortcuts.utils

import android.util.Log
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Utility class for debugging API calls and network issues
 */
object DebugUtils {
    private const val TAG = "FireflyDebug"

    /**
     * Logs detailed information about a request
     */
    fun logRequest(request: Request) {
        try {
            val url = request.url.toString()
            val method = request.method
            val headers = request.headers

            val message = StringBuilder()
            message.appendLine("🔍 REQUEST DETAILS:")
            message.appendLine("→ URL: $url")
            message.appendLine("→ Method: $method")
            message.appendLine("→ Headers:")
            headers.forEach { (name, value) ->
                // Don't log full auth token for security
                val displayValue = if (name.equals("Authorization", ignoreCase = true)) {
                    if (value.startsWith("Bearer ", ignoreCase = true)) {
                        "Bearer ${value.substring(7).take(10)}..."
                    } else {
                        "${value.take(10)}..."
                    }
                } else {
                    value
                }
                message.appendLine("  • $name: $displayValue")
            }

            // Parse and log query parameters
            try {
                val urlObj = URL(url)
                val query = urlObj.query
                if (!query.isNullOrEmpty()) {
                    message.appendLine("→ Query Parameters:")
                    query.split("&").forEach { param ->
                        val keyValue = param.split("=", limit = 2)
                        if (keyValue.size == 2) {
                            val key = keyValue[0]
                            val value =
                                URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name())
                            message.appendLine("  • $key: $value")
                        }
                    }
                }
            } catch (e: Exception) {
                message.appendLine("→ Could not parse query parameters: ${e.message}")
            }

            Log.d(TAG, message.toString())
        } catch (e: Exception) {
            Log.e(TAG, "Error logging request: ${e.message}", e)
        }
    }

    /**
     * Logs detailed information about a response
     */
    fun logResponse(response: Response) {
        try {
            val code = response.code
            val message = response.message
            val headers = response.headers

            val builder = StringBuilder()
            builder.appendLine("📥 RESPONSE DETAILS:")
            builder.appendLine("→ Code: $code")
            builder.appendLine("→ Message: $message")
            builder.appendLine("→ Headers:")
            headers.forEach { (name, value) ->
                builder.appendLine("  • $name: $value")
            }

            Log.d(TAG, builder.toString())
        } catch (e: Exception) {
            Log.e(TAG, "Error logging response: ${e.message}", e)
        }
    }

    /**
     * Logs an error that occurred during API communication
     */
    fun logApiError(e: Exception, context: String = "API call") {
        val errorType = when (e) {
            is IOException -> "IO Error (network issue)"
            is IllegalStateException -> "State Error"
            is IllegalArgumentException -> "Argument Error"
            else -> "Unexpected Error (${e.javaClass.simpleName})"
        }

        Log.e(TAG, "❌ $errorType in $context: ${e.message}", e)
    }

    /**
     * Validates a URL and logs detailed information about any issues
     */
    fun validateUrl(url: String): Boolean {
        return try {
            val urlObj = URL(url)
            val protocol = urlObj.protocol
            val host = urlObj.host
            val port = urlObj.port
            val path = urlObj.path

            val isValid = protocol.matches("^https?$".toRegex()) && host.isNotEmpty()

            val builder = StringBuilder()
            builder.appendLine("🌐 URL Validation: ${if (isValid) "Valid ✓" else "Invalid ✗"}")
            builder.appendLine("→ Full URL: $url")
            builder.appendLine("→ Protocol: $protocol ${if (protocol.matches("^https?$".toRegex())) "✓" else "✗"}")
            builder.appendLine("→ Host: $host ${if (host.isNotEmpty()) "✓" else "✗"}")
            builder.appendLine("→ Port: ${if (port == -1) "Default" else port}")
            builder.appendLine("→ Path: $path")

            Log.d(TAG, builder.toString())
            isValid
        } catch (e: Exception) {
            Log.e(TAG, "URL validation failed for '$url': ${e.message}", e)
            false
        }
    }

    /**
     * Analyzes a 404 error, trying to determine if it's a server, base URL or path issue
     */
    fun analyze404Error(baseUrl: String, endpoint: String) {
        try {
            Log.d(TAG, "🔍 Analyzing 404 Error:")
            Log.d(TAG, "→ Base URL: $baseUrl")
            Log.d(TAG, "→ Endpoint: $endpoint")

            // Validate base URL format
            val isValidUrl = validateUrl(baseUrl)
            if (!isValidUrl) {
                Log.d(TAG, "→ Issue detected: Malformed base URL")
                return
            }

            // Check common issues with Firefly III URLs
            val baseUrlObj = URL(baseUrl)
            val path = baseUrlObj.path

            if (path.isNotEmpty() && path != "/") {
                Log.d(TAG, "→ Note: Base URL contains a path: $path")
                Log.d(
                    TAG,
                    "→ This might cause path resolution issues if the API expects to be at the root"
                )
            }

            // Check if the endpoint uses the v1 namespace
            if (!endpoint.startsWith("/v1/") && !endpoint.startsWith("v1/")) {
                Log.d(
                    TAG,
                    "→ Warning: Endpoint doesn't start with 'v1/' which is required for Firefly III API"
                )
            }

            Log.d(TAG, "→ Full URL being accessed: $baseUrl/$endpoint")

        } catch (e: Exception) {
            Log.e(TAG, "Error during 404 analysis: ${e.message}", e)
        }
    }
}