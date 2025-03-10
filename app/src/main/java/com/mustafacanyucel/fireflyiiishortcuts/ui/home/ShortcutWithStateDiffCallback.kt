package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import androidx.recyclerview.widget.DiffUtil

class ShortcutWithStateDiffCallback: DiffUtil.ItemCallback<ShortcutWithState>() {
    override fun areItemsTheSame(oldItem: ShortcutWithState, newItem: ShortcutWithState): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ShortcutWithState,
        newItem: ShortcutWithState
    ): Boolean {
        return oldItem == newItem
    }
}