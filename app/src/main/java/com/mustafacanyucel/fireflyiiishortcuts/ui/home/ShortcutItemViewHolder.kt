package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mustafacanyucel.fireflyiiishortcuts.databinding.HomeShortcutItemBinding
import com.mustafacanyucel.fireflyiiishortcuts.ui.listener.IShortcutClickListener
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState

class ShortcutItemViewHolder private constructor(
    private val binding: HomeShortcutItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ShortcutWithState, clickListener: IShortcutClickListener) {
        binding.shortcut = item.shortcut
        binding.state = item.state
        binding.shortcutClickListener = clickListener
        binding.executePendingBindings()
    }

    fun updateState(newState: ShortcutState) {
        binding.state = newState
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ShortcutItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HomeShortcutItemBinding.inflate(layoutInflater, parent, false)
            return ShortcutItemViewHolder(binding)
        }
    }
}