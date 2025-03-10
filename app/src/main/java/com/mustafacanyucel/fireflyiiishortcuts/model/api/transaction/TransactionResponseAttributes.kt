package com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction

import com.google.gson.annotations.SerializedName

data class TransactionResponseAttributes(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val user: String,
    @SerializedName("group_title") val groupTitle: String?,
    val transactions: List<TransactionResponseItem>
)
