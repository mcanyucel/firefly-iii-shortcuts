package com.mustafacanyucel.fireflyiiishortcuts.widget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.mustafacanyucel.fireflyiiishortcuts.MainActivity
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Service for widget-initiated shortcut executions.
 * This service acts as a bridge between widget clicks and the ShortcutExecutor
 */
@AndroidEntryPoint
class ShortcutWidgetExecutionService : Service() {
    @Inject
    lateinit var widgetExecutor: ShortcutWidgetExecutor

    @Inject
    lateinit var widgetRepository: ShortcutWidgetRepository

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started with action: ${intent?.action}")

        if (intent?.action == ACTION_STOP_SERVICE) {
            Log.d(TAG, "Received stop service intent")
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf(startId)
            return START_NOT_STICKY
        }

        // Start as a foreground service immediately to comply with Android O+ requirements
        startForeground(FOREGROUND_SERVICE_ID, createNotification("Processing shortcut..."))

        if (intent == null) {
            Log.e(TAG, "Received null intent")
            stopForeground(true)
            stopSelf(startId)
            return START_NOT_STICKY
        }

        val shortcutId = intent.getLongExtra(ShortcutWidgetProvider.EXTRA_SHORTCUT_ID, -1L)
        if (shortcutId == -1L) {
            Log.e(TAG, "Received invalid shortcut ID")
            stopForeground(true)
            stopSelf(startId)
            return START_NOT_STICKY
        }

        Log.d(TAG, "Processing shortcut execution request for ID: $shortcutId")

        val currentState = widgetRepository.getShortcutState(shortcutId)
        if (currentState == ShortcutState.QUEUED || currentState == ShortcutState.RUNNING) {
            Log.d(TAG, "Shortcut execution is already in progress")
            stopForeground(true)
            stopSelf(startId)
            return START_NOT_STICKY
        }

        try {
            widgetExecutor.executeShortcut(shortcutId)
        } catch (e: Exception) {
            Log.e(TAG, "Error executing shortcut", e)
            widgetRepository.updateShortcutState(shortcutId, ShortcutState.FAILURE)
            ShortcutWidgetProvider.notifyShortcutStateChanged(
                applicationContext,
                shortcutId,
                ShortcutState.FAILURE
            )
        }

        // The actual execution happens in the widgetExecutor which will call stopSelf when done
        return START_STICKY
    }

    private fun createNotificationChannel() {
        val name = "Widget Execution"
        val descriptionText = "Shows the status of shortcut executions from the widget"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(message: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Firefly Shortcuts")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_play_inverse_24)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    fun updateNotification(message: String) {
        val notification = createNotification(message)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(FOREGROUND_SERVICE_ID, notification)
    }

    fun completeExecution() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    companion object {
        private const val TAG = "ShortcutWidgetExecution"
        private const val FOREGROUND_SERVICE_ID = 2222
        private const val CHANNEL_ID = "widget_execution_channel"
        const val ACTION_STOP_SERVICE = "com.mustafacanyucel.fireflyiiishortcuts.widget.STOP_SERVICE"

    }
}