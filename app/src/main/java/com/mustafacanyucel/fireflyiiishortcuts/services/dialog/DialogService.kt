package com.mustafacanyucel.fireflyiiishortcuts.services.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.model.EventData
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import javax.inject.Inject

class DialogService @Inject constructor(
    private val activity: Activity
) : IDialogService {
    override fun showDialogSnackbar(eventData: EventData) {
        val rootView = activity.findViewById<View>(android.R.id.content)
        val backgroundTintIndex = when (eventData.eventType) {
            EventType.SUCCESS -> R.color.material_green_300
            EventType.INFO -> R.color.material_blue_300
            EventType.WARNING -> R.color.material_amber_300
            EventType.ERROR -> R.color.material_red_300
        }
        hideKeyboard()
        Snackbar.make(rootView, eventData.message, Snackbar.LENGTH_LONG)
            .apply {
                setBackgroundTint(ContextCompat.getColor(activity, backgroundTintIndex))
                if (eventData.action != null) {
                    setActionTextColor(Color.WHITE)
                    setAction(eventData.actionTitle ?: "Action") {
                        eventData.action.invoke()
                    }
                }
            }
            .show()
    }

    private fun hideKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity.currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}