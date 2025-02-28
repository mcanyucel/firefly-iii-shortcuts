package com.mustafacanyucel.fireflyiiishortcuts.model.api.bill

data class BillData(
    val type: String,
    val id: String,
    val attributes: BillAttributes
)