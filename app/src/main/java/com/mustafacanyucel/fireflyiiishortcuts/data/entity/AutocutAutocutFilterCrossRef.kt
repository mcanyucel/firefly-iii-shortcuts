package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "autocut_autocutfilter_cross_ref",
    primaryKeys = ["autocutId", "autocutFilterId"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = AutocutEntity::class,
            parentColumns = ["id"],
            childColumns = ["autocutId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        ),
        androidx.room.ForeignKey(
            entity = AutocutFilterEntity::class,
            parentColumns = ["id"],
            childColumns = ["autocutFilterId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [Index("autocutFilterId")]

)

data class AutocutAutocutFilterCrossRef(
    val autocutId: Long, val autocutFilterId: Long
)

