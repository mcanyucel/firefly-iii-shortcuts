package com.mustafacanyucel.fireflyiiishortcuts.ui.bindingadapter

import android.animation.ObjectAnimator
import android.text.format.DateUtils
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState

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

        @JvmStatic
        @BindingAdapter("icon")
        fun ImageButton.setIcon(shortcutState: ShortcutState?) {
            when (shortcutState) {
                ShortcutState.IDLE -> setImageResource(R.drawable.ic_run_48dp)
                ShortcutState.QUEUED -> setImageResource(R.drawable.baseline_av_timer_48)
                ShortcutState.RUNNING -> setImageResource(R.drawable.ic_sync_48dp)
                ShortcutState.SUCCESS -> setImageResource(R.drawable.baseline_done_48)
                ShortcutState.FAILURE -> setImageResource(R.drawable.baseline_cancel_48)
                null -> setImageResource(R.drawable.ic_run_48dp)
            }

            val existingAnimator = getTag(R.id.animator_tag) as? ObjectAnimator
            existingAnimator?.removeAllListeners()
            existingAnimator?.cancel()

            rotation = 0f

            if (shortcutState == ShortcutState.RUNNING) {
                val newAnimator = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f).apply {
                    duration = 1000
                    repeatCount = ObjectAnimator.INFINITE
                    interpolator = LinearInterpolator()
                    start()
                }

                setTag(R.id.animator_tag, newAnimator)
            }
            else {
                setTag(R.id.animator_tag, null)
            }
        }
    }
}
