package com.mustafacanyucel.fireflyiiishortcuts.model.api.budget

import com.mustafacanyucel.fireflyiiishortcuts.model.api.Meta

data class BudgetResponse(
    val data: List<BudgetData>, val meta: Meta
)
