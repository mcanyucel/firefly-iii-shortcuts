package com.mustafacanyucel.fireflyiiishortcuts.model.api.tag

import com.mustafacanyucel.fireflyiiishortcuts.model.api.Meta

data class TagResponse (
    val data: List<TagData>,
    val meta: Meta
)