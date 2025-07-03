package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents an AutocutFilter with its associated tags.
 * This is a relationship class that Room uses to query AutocutFilters with their associated tags.
 */
data class AutocutFilterWithTags(
    @Embedded
    val autocutFilter: AutocutFilterEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = androidx.room.Junction(
            value = AutocutFilterTagCrossRef::class,
            parentColumn = "autocutFilterId",
            entityColumn = "tagId"
        )
    )

    val tags: List<TagEntity>
)
