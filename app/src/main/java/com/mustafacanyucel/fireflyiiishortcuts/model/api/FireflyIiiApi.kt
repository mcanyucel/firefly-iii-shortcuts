package com.mustafacanyucel.fireflyiiishortcuts.model.api

import retrofit2.http.GET
import retrofit2.http.Query


interface FireflyIiiApi {
    @GET("v1/accounts")
    suspend fun getAccounts(
        @Query("limit") limit: Int = 100,
        @Query("page") page: Int = 1,
        @Query("type") type: String? = null,
        @Query("date") date: String? = null,
    ): AccountResponse

    @GET("v1/categories")
    suspend fun getCategories(
        @Query("limit") limit: Int = 100,
        @Query("page") page: Int = 1
    ): CategoryResponse

    @GET("v1/budgets")
    suspend fun getBudgets(
        @Query("limit") limit: Int = 100,
        @Query("page") page: Int = 1
    ): BudgetResponse
}