package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mustafacanyucel.fireflyiiishortcuts.databinding.ItemShortcutBinding
import com.mustafacanyucel.fireflyiiishortcuts.ui.diff.ShortcutModelDiffCallback
import com.mustafacanyucel.fireflyiiishortcuts.ui.listener.IShortcutClickListener
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel
import java.text.NumberFormat
import java.util.Locale

class ShortcutItemEditAdapter(
    private val listener: IShortcutClickListener
) : ListAdapter<ShortcutModel, ShortcutItemEditAdapter.ShortcutViewHolder>(ShortcutModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        val binding = ItemShortcutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShortcutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        val shortcut = getItem(position)
        holder.bind(shortcut)
    }

    inner class ShortcutViewHolder(
        private val binding: ItemShortcutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val shortcut = getItem(position)
                    listener.onShortcutClicked(shortcut)
                }
            }
        }

        fun bind(shortcutModel: ShortcutModel) {
            binding.apply {
                // Format the amount with currency symbol
                val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
                val formattedAmount = numberFormat.format(shortcutModel.amount)

                tvShortcutName.text = shortcutModel.name

                // Display from/to accounts if available
                val fromAccount = shortcutModel.fromAccountEntity?.name ?: "N/A"
                val toAccount = shortcutModel.toAccountEntity?.name ?: "N/A"
                tvAccountInfo.text = "$fromAccount → $toAccount"

                // Show category if available
                tvCategory.text = shortcutModel.categoryEntity?.name ?: "No category"

                // Show the amount
                tvAmount.text = formattedAmount

                // Show the first few tags (if any)
                val tagNames = shortcutModel.tagEntities.take(3).joinToString(", ") { it.tag }
                tvTags.text = if (tagNames.isNotEmpty()) tagNames else "No tags"

                // Show additional tag count if there are more than 3 tags
                val extraTagCount = shortcutModel.tagEntities.size - 3
                if (extraTagCount > 0) {
                    tvTags.append(" +$extraTagCount more")
                }

                // Show the last used date if available
                tvLastUsed.text =
                    if (shortcutModel.lastUsed != null && shortcutModel.lastUsed > 0) {
                        "Last used: ${formatDate(shortcutModel.lastUsed)}"
                    } else {
                        "Never used"
                    }
            }
        }

        private fun formatDate(timestamp: Long): String {
            // Simple date formatting - can be replaced with a more robust solution
            val date = java.util.Date(timestamp)
            return android.text.format.DateFormat.getDateFormat(binding.root.context).format(date)
        }
    }

}