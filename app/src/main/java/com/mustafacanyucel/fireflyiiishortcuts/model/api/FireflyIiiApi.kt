package com.mustafacanyucel.fireflyiiishortcuts.model.api

import com.mustafacanyucel.fireflyiiishortcuts.model.api.account.AccountResponse
import com.mustafacanyucel.fireflyiiishortcuts.model.api.bill.BillResponse
import com.mustafacanyucel.fireflyiiishortcuts.model.api.budget.BudgetResponse
import com.mustafacanyucel.fireflyiiishortcuts.model.api.category.CategoryResponse
import com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank.PiggybankResponse
import com.mustafacanyucel.fireflyiiishortcuts.model.api.tag.TagResponse
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

    @GET("v1/tags")
    suspend fun getTags(
        @Query("limit") limit: Int = 100,
        @Query("page") page: Int = 1
    ): TagResponse

    @GET("v1/piggy-banks")
    suspend fun getPiggyBanks(
        @Query("limit") limit: Int = 100,
        @Query("page") page: Int = 1
    ): PiggybankResponse

    @GET("v1/bills")
    suspend fun getBills(
        @Query("limit") limit: Int = 100,
        @Query("page") page: Int = 1
    ): BillResponse
}