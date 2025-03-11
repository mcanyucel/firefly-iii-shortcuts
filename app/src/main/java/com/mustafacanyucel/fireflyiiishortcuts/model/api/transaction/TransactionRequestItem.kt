package com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction

import com.google.gson.annotations.SerializedName

data class TransactionRequestItem(
    val type: String,
    @SerializedName("date") val dateUTCString: String,
    val amount: String,
    val description: String,
    val order: Int = 0,
    val reconciled: Boolean = false,
    @SerializedName("source_id") val fromAccountId: String,
    @SerializedName("source_name") val fromAccountName: String,
    @SerializedName("destination_id") val toAccountId: String,
    @SerializedName("destination_name") val toAccountName: String,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("budget_id") val budgetId: String?,
    @SerializedName("budget_name") val budgetName: String?,
    @SerializedName("bill_id") val billId: String?,
    @SerializedName("bill_name") val billName: String?,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("piggy_bank_id") val piggyBankId: String?,
    @SerializedName("piggy_bank_name") val piggyBankName: String?,
    val tags: List<String>?
)
