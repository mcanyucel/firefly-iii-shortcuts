package com.mustafacanyucel.fireflyiiishortcuts.model.api.account

import com.mustafacanyucel.fireflyiiishortcuts.model.api.Meta

data class AccountResponse(
    val data: List<AccountData>,
    val meta: Meta
)
