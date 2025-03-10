package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState

data class ShortcutWithState(
    val shortcut: ShortcutModel,
    val state: ShortcutState = ShortcutState.IDLE
) {
    val id: Long get() = shortcut.id
}
