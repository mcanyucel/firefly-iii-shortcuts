package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustafacanyucel.fireflyiiishortcuts.model.api.bill.BillData

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey val id: String,
    val createdAt: String,
    val updatedAt: String?,
    val currencyId: String,
    val currencyCode: String,
    val name: String,
    val notes: String?
) {
    companion object {
        fun fromApiModel(billData: BillData): BillEntity {
            return BillEntity(
                id = billData.id,
                createdAt = billData.attributes.createdAt,
                updatedAt = billData.attributes.updatedAt,
                currencyId = billData.attributes.currencyId,
                currencyCode = billData.attributes.currencyCode,
                name = billData.attributes.name,
                notes = billData.attributes.notes
            )
        }
    }
}