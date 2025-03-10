package com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction

import com.google.gson.annotations.SerializedName

data class TransactionResponseItem(
    val user: String,
    @SerializedName("transaction_journal_id") val transactionJournalId: String,
    val date: String
)