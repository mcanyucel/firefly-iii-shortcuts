package com.mustafacanyucel.fireflyiiishortcuts.model.api.bill

import com.mustafacanyucel.fireflyiiishortcuts.model.api.Meta

data class BillResponse(
    val data: List<BillData>,
    val meta: Meta
)
