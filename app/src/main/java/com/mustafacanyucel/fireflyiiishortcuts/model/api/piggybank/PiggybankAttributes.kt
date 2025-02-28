package com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank

import com.google.gson.annotations.SerializedName

data class PiggybankAttributes(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
    val name: String,
    @SerializedName("currency_id") val currencyId: String,
    @SerializedName("currency_code") val currencyCode: String,
    val notes: String?
)