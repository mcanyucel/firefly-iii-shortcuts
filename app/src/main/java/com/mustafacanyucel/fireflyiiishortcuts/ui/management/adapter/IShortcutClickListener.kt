package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel

interface IShortcutClickListener {
    fun onShortcutClicked(shortcut: ShortcutModel)
}