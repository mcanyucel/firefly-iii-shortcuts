package com.mustafacanyucel.fireflyiiishortcuts.model.api

import com.google.gson.annotations.SerializedName

data class Pagination(
    val total: Int,
    val count: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("total_pages") val totalPages: Int
)