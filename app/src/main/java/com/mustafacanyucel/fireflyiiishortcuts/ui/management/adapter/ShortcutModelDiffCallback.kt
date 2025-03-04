package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel

class ShortcutModelDiffCallback: DiffUtil.ItemCallback<ShortcutModel>(
    ) {
    override fun areItemsTheSame(oldItem: ShortcutModel, newItem: ShortcutModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShortcutModel, newItem: ShortcutModel): Boolean {
        return oldItem == newItem
    }
}