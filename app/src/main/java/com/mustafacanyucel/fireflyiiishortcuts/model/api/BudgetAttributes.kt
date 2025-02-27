package com.mustafacanyucel.fireflyiiishortcuts.model.api

import com.google.gson.annotations.SerializedName

data class BudgetAttributes(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val name: String,
    val active: Boolean,
    val notes: String?,
    @SerializedName("currency_id") val currencyId: String?,
    @SerializedName("currency_code") val currencyCode: String?
)