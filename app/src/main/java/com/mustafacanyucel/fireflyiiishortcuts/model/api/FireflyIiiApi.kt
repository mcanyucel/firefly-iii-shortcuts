package com.mustafacanyucel.fireflyiiishortcuts.model.api

import retrofit2.http.GET
import retrofit2.http.Query


interface FireflyIiiApi {
    @GET("v1/accounts")
    suspend fun getAccounts(
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1,
        @Query("type") type: String? = null,
        @Query("date") date: String? = null,
    ): AccountResponse
}