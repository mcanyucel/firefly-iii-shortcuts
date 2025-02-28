package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustafacanyucel.fireflyiiishortcuts.model.api.category.CategoryData

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey override val id: String,
    val name: String,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String,
    val nativeCurrencyId: String?,
    val nativeCurrencyCode: String?
): IEntity {
    companion object {
        fun fromApiModel(categoryData: CategoryData): CategoryEntity {
            return CategoryEntity(
                id = categoryData.id,
                name = categoryData.attributes.name,
                notes = categoryData.attributes.notes,
                createdAt = categoryData.attributes.createdAt,
                updatedAt = categoryData.attributes.updatedAt,
                nativeCurrencyId = categoryData.attributes.nativeCurrencyId,
                nativeCurrencyCode = categoryData.attributes.nativeCurrencyCode
            )
        }
    }
}