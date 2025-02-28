package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Junction table for the many-to-many relationship between Shortcuts and Tags
 */
@Entity(
    tableName = "shortcut_tag_cross_ref",
    primaryKeys = ["shortcutId", "tagId"],
    foreignKeys = [ForeignKey(
        entity = ShortcutEntity::class,
        parentColumns = ["id"],
        childColumns = ["shortcutId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = TagEntity::class,
        parentColumns = ["id"],
        childColumns = ["tagId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("tagId")]
)
data class ShortcutTagCrossRef(
    val shortcutId: Long, val tagId: String
)
