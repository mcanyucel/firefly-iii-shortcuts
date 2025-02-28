package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank.PiggybankData

@Entity(tableName = "piggybanks")
data class PiggybankEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String?,
    val currencyId: String,
    val currencyCode: String?,
    val notes: String?
) {
    companion object {
        fun fromApiModel(piggybankData: PiggybankData): PiggybankEntity {
            return PiggybankEntity(
                id = piggybankData.id,
                name = piggybankData.attributes.name,
                createdAt = piggybankData.attributes.createdAt,
                updatedAt = piggybankData.attributes.updatedAt,
                currencyId = piggybankData.attributes.currencyId,
                currencyCode = piggybankData.attributes.currencyCode,
                notes = piggybankData.attributes.notes
            )
        }
    }
}