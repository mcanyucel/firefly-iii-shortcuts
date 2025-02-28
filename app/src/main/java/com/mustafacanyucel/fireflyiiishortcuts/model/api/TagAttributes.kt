package com.mustafacanyucel.fireflyiiishortcuts.model.api

import com.google.gson.annotations.SerializedName

data class TagAttributes (
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
    val tag: String,
    val date: String?,
    val description: String?
)
