package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustafacanyucel.fireflyiiishortcuts.model.api.tag.TagData

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey override val id: String,
    val createdAt: String,
    val updatedAt: String?,
    val tag: String,
    val date: String?,
    val description: String?
) : IEntity {
    companion object {
        fun fromApiModel(tagData: TagData): TagEntity {
            return TagEntity(
                id = tagData.id,
                createdAt = tagData.attributes.createdAt,
                updatedAt = tagData.attributes.updatedAt,
                tag = tagData.attributes.tag,
                date = tagData.attributes.date,
                description = tagData.attributes.description
            )
        }
    }
}