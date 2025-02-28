package com.mustafacanyucel.fireflyiiishortcuts.ui.sync

data class DbChangeResult(
    val added: Int,
    val updated: Int,
    val removed: Int,
    val deletedShortcuts: Int
)
