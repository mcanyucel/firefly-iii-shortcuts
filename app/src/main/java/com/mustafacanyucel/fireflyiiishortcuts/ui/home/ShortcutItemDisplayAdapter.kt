package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.listener.IShortcutClickListener
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState

class ShortcutItemDisplayAdapter(
    private val clickListener: IShortcutClickListener
) : ListAdapter<ShortcutWithState, ShortcutItemViewHolder>(ShortcutWithStateDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutItemViewHolder {
        return ShortcutItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ShortcutItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onBindViewHolder(
        holder: ShortcutItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == PAYLOAD_STATE_CHANGED) {
            holder.updateState(getItem(position).state)
        }
        else {
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    fun updateItemState(shortcutId: Long, newState: ShortcutState) {
        val position = currentList.indexOfFirst { it.id == shortcutId }
        if (position != -1) {
            val item = getItem(position)
            val updatedItem = item.copy(state = newState)

            val newList = currentList.toMutableList()
            newList[position] = updatedItem
            submitList(newList) {
                notifyItemChanged(position, PAYLOAD_STATE_CHANGED)
            }

        }
    }

    fun updateStates(stateMap: Map<Long, ShortcutState>) {
        val newList = currentList.map { item ->
            val newState = stateMap[item.id] ?: item.state
            if (newState != item.state) item.copy(state = newState) else item
        }
        submitList(newList)

    }

    companion object {
        const val PAYLOAD_STATE_CHANGED = "state_changed"
    }
}