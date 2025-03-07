package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.diff.ShortcutModelDiffCallback
import com.mustafacanyucel.fireflyiiishortcuts.ui.listener.IShortcutClickListener
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel

class ShortcutItemDisplayAdapter(
    private val clickListener: IShortcutClickListener
) : ListAdapter<ShortcutModel, ShortcutItemViewHolder>(ShortcutModelDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutItemViewHolder {
        return ShortcutItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ShortcutItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}