package com.mustafacanyucel.fireflyiiishortcuts.ui.bindingadapter

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel

class ShortcutBindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("accounts")
        fun TextView.setAccounts(shortcutModel: ShortcutModel?) {
            if (shortcutModel != null) {
                val fromAccount =
                    shortcutModel.fromAccountEntity?.name ?: context.getString(R.string.n_a)
                val toAccount =
                    shortcutModel.toAccountEntity?.name ?: context.getString(R.string.n_a)
                text = context.getString(R.string.account_transfer_format, fromAccount, toAccount)
            } else {
                text = context.getString(R.string.n_a)
            }
        }

        @JvmStatic
        @BindingAdapter("tags")
        fun TextView.setTags(shortcutModel: ShortcutModel?) {
            text = if (shortcutModel == null || shortcutModel.tagEntities.isEmpty()) {
                context.getString(R.string.no_tags)
            } else {
                val tagCount = shortcutModel.tagEntities.size

                if (tagCount < 3) {
                    shortcutModel.tagEntities.joinToString(", ") { it.tag }
                } else {
                    shortcutModel.tagEntities.subList(0, 2).joinToString(", ") + ", ..."
                }
            }
        }

        @JvmStatic
        @BindingAdapter("lastUsed")
        fun TextView.setLastUsed(shortcutModel: ShortcutModel?) {
            text = if (shortcutModel?.lastUsed != null && shortcutModel.lastUsed != 0L) {
                DateUtils.getRelativeTimeSpanString(shortcutModel.lastUsed)
            } else {
                context.getString(R.string.never)
            }
        }
    }
}
