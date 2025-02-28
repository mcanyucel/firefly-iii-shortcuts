package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustafacanyucel.fireflyiiishortcuts.model.api.account.AccountData

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val name: String,
    val accountType: String,
    val accountRole: String?,
    val currencyCode: String?,
    val currencySymbol: String?,
    val currentBalance: String,
    val iban: String?,
    val accountNumber: String?,
    val notes: String?,
    val active: Boolean,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromApiModel(accountData: AccountData): AccountEntity {
            return AccountEntity(
                id = accountData.id,
                type = accountData.type,
                name = accountData.attributes.name,
                accountType = accountData.attributes.type,
                accountRole = accountData.attributes.accountRole,
                currencyCode = accountData.attributes.currencyCode,
                currencySymbol = accountData.attributes.currencySymbol,
                currentBalance = accountData.attributes.currentBalance,
                iban = accountData.attributes.iban,
                accountNumber = accountData.attributes.accountNumber,
                notes = accountData.attributes.notes,
                active = accountData.attributes.active
            )
        }
    }
}
