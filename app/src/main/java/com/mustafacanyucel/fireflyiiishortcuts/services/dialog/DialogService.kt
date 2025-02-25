package com.mustafacanyucel.fireflyiiishortcuts.services.dialog

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class DialogService @Inject constructor(
    private val activity: Activity
) : IDialogService {
    override fun showErrorSnackbar(message: String, action: (() -> Unit)?, actionTitle: String?) {
        val rootView = activity.findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
            .apply {
                setBackgroundTint(Color.RED)
            }
        if (action != null) {
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.setAction(actionTitle ?: "Action") {
                action.invoke()
            }
        }
        snackbar.show()
    }
}