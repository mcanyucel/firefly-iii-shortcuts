package com.mustafacanyucel.fireflyiiishortcuts.model.api

import com.google.gson.annotations.SerializedName

data class AccountAttributes(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val active: Boolean,
    val order: Int,
    val name: String,
    val type: String,
    @SerializedName("account_role") val accountRole: String?,
    @SerializedName("currency_id") val currencyId: String,
    @SerializedName("currency_code") val currencyCode: String,
    @SerializedName("currency_symbol") val currencySymbol: String,
    @SerializedName("currency_decimal_places") val currencyDecimalPlaces: Int,
    @SerializedName("current_balance") val currentBalance: String,
    @SerializedName("native_current_balance") val nativeCurrentBalance: String,
    @SerializedName("current_balance_date") val currentBalanceDate: String,
    val notes: String?,
    val iban: String?,
    val bic: String?,
    @SerializedName("account_number") val accountNumber: String?,
    @SerializedName("include_net_worth") val includeNetWorth: Boolean
    // We're not including all fields to keep the model simple
    // Add other fields as needed
)
