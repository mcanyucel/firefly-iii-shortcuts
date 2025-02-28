package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Class representing a shortcut with its related tags
 * This is a relationship class that Room uses to query shortcuts with their associated tags
 */
data class ShortcutWithTags(
    @Embedded
    val shortcut: ShortcutEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ShortcutTagCrossRef::class,
            parentColumn = "shortcutId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)
