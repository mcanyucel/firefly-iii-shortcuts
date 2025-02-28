package com.mustafacanyucel.fireflyiiishortcuts.model.api.piggybank

import com.mustafacanyucel.fireflyiiishortcuts.model.api.Meta

data class PiggybankResponse(
    val data: List<PiggybankData>,
    val meta: Meta
)
