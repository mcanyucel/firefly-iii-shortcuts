package com.mustafacanyucel.fireflyiiishortcuts.services.repository

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val message: String,
        val errorCode: Int? = null,
        val exception: Exception? = null
    ) : ApiResult<Nothing>()
}