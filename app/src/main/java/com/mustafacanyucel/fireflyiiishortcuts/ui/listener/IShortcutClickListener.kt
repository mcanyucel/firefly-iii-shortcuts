package com.mustafacanyucel.fireflyiiishortcuts.ui.listener

import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel

interface IShortcutClickListener {
    fun onShortcutClicked(shortcut: ShortcutModel)
}