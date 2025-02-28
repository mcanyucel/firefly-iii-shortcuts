package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

class ApiHelper {
    companion object {
        fun getHttpErrorMessage(code: Int, errorBody: String?): String {
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
}