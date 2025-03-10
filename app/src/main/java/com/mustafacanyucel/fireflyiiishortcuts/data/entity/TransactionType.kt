package com.mustafacanyucel.fireflyiiishortcuts.data.entity

enum class TransactionType {
    TRANSFER,
    DEPOSIT,
    WITHDRAWAL;

    override fun toString(): String = this.name.lowercase()
}