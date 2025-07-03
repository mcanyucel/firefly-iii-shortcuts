package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "autocuts"
)

data class AutocutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val template: String,
    val icon: String? = null
)
