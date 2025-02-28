package com.mustafacanyucel.fireflyiiishortcuts.model.api.category

import com.mustafacanyucel.fireflyiiishortcuts.model.api.Meta

data class CategoryResponse(
    val data: List<CategoryData>,
    val meta: Meta
)