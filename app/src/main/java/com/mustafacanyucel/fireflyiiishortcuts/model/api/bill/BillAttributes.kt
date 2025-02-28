package com.mustafacanyucel.fireflyiiishortcuts.model.api.bill

import com.google.gson.annotations.SerializedName

data class BillAttributes(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("currency_id") val currencyId: String,
    @SerializedName("currency_code") val currencyCode: String,
    val name: String,
    val notes: String?
)
