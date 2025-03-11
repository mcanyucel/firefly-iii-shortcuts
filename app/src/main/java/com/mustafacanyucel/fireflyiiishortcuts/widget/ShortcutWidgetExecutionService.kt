package com.mustafacanyucel.fireflyiiishortcuts.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Service for widget-initiated shortcut executions.
 * This service acts as a bridge between widget clicks and the ShortcutExecutor
 */
@AndroidEntryPoint
class ShortcutWidgetExecutionService : Service() {
    @Inject lateinit var widgetExecutor: ShortcutWidgetExecutor

    @Inject lateinit var widgetRepository: ShortcutWidgetRepository

    override fun onBind(p0: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand() called with: intent = $intent, flags = $flags, startId = $startId")

        if (intent == null) {
            Log.e(TAG, "onStartCommand() called with null intent")
            stopSelf(startId)
            return START_NOT_STICKY
        }

        val shortcutId = intent.getLongExtra(ShortcutWidgetProvider.EXTRA_SHORTCUT_ID, -1L)
        if (shortcutId == -1L) {
            Log.e(TAG, "onStartCommand() called with invalid shortcutId")
            stopSelf(startId)
            return START_NOT_STICKY
        }

        Log.d(TAG, "Processing shortcut execution request for shortcutId: $shortcutId")

        val currentState = widgetRepository.getShortcutState(shortcutId)
        if (currentState == ShortcutState.QUEUED || currentState == ShortcutState.RUNNING) {
            Log.d(TAG, "Shortcut is already running or queued, skipping execution")
            stopSelf(startId)
            return START_NOT_STICKY
        }

        // execute
        try {
            widgetExecutor.executeShortcut(shortcutId)
        } catch (e: Exception) {
            Log.e(TAG, "Error executing shortcut", e)
            widgetRepository.updateShortcutState(shortcutId, ShortcutState.FAILURE)
            ShortcutWidgetProvider.notifyShortcutStateChanged(applicationContext, shortcutId, ShortcutState.FAILURE)
        }

        // this service is no longer needed
        stopSelf(startId)
        return START_NOT_STICKY
    }

    companion object {
        private const val TAG = "ShortcutWidgetExecution"
    }
}