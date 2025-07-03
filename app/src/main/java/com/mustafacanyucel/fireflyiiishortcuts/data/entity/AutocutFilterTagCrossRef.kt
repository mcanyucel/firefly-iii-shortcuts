package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "autocutfilter_tag_cross_ref",
    primaryKeys = ["autocutFilterId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = AutocutFilterEntity::class,
            parentColumns = ["id"],
            childColumns = ["autocutFilterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tagId")]
)

data class AutocutFilterTagCrossRef(
    val autocutFilterId: Long, val tagId: String
)
