package com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction

import com.google.gson.annotations.SerializedName

data class TransactionRequest(
    @SerializedName("error_if_duplicate_hash") val errorIfDuplicateHash: Boolean = false,
    @SerializedName("apply_rules") val applyRules: Boolean = true,
    @SerializedName("fire_webhooks") val fireWebhooks: Boolean = true,
    @SerializedName("group_title") val groupTitle: String? = null, // Future support for split transactions
    val transactions: List<TransactionRequestItem>
)
