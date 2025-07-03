package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents an Autocut with its associated AutocutFilters.
 * This is a relationship class that Room uses to query Autocuts with their associated AutocutFilters.
 */
data class AutocutWithAutocutFilters(
    @Embedded
    val autocut: AutocutEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = androidx.room.Junction(
            value = AutocutAutocutFilterCrossRef::class,
            parentColumn = "autocutId",
            entityColumn = "autocutFilterId"
        )
    )
    val autocutFilters: List<AutocutFilterWithTags>
)