package com.mustafacanyucel.fireflyiiishortcuts.model.api

import com.google.gson.annotations.SerializedName

data class CategoryAttributes(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val name: String,
    val notes: String?,
    @SerializedName("native_currency_id") val nativeCurrencyId: String,
    @SerializedName("native_currency_code") val nativeCurrencyCode: String
    // the rest are not used for now
)
