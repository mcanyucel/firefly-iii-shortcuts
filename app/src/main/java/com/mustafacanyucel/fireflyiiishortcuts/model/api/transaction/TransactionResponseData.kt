package com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction

data class TransactionResponseData(
    val type: String,
    val id: String,
    val attributes: TransactionResponseAttributes
)
