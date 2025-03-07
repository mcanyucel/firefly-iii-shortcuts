package com.mustafacanyucel.fireflyiiishortcuts.services.firefly

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.getParcelableExtraCompat
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutExecutionData
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

class ShortcutExecutionService : Service() {
    private val _shortcutQueue = ConcurrentLinkedQueue<ShortcutExecutionData>()
    private val _binder = LocalBinder()
    private var _isProcessing = false


    inner class LocalBinder : Binder() {
        fun getService(): ShortcutExecutionService = this@ShortcutExecutionService
    }

    override fun onBind(p0: Intent?): IBinder {
        return _binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification("Starting Execution"))
        when (intent?.action) {
            ACTION_CANCEL_ALL -> {
                _shortcutQueue.clear()
                updateNotification()
            }

            ACTION_SHORTCUTS_CANCELLED -> {
                val shortcutExecutionData =
                    intent.getParcelableExtraCompat<ShortcutExecutionData>(EXTRA_SHORTCUT_DATA)
                _shortcutQueue.removeIf { it.id == shortcutExecutionData?.id }
                updateNotification()
            }

            else -> {
                val executionData = intent?.getParcelableExtraCompat<ShortcutExecutionData>(
                    EXTRA_SHORTCUT_DATA
                )
                executionData?.let {
                    enqueueShortcut(it)
                } ?: stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    fun enqueueShortcut(shortcutExecutionData: ShortcutExecutionData) {
        Log.d(TAG, "Enqueued shortcut: ${shortcutExecutionData.name}")
        _shortcutQueue.add(shortcutExecutionData)

        if (!_isProcessing) {
            startForeground(NOTIFICATION_ID, createNotification("Processing shortcuts"))
            processQueue()
        } else {
            updateNotification("Added to queue: ${shortcutExecutionData.name}")
        }
    }

    private fun processQueue() {
        Log.d(TAG, "Processing queue")
        _isProcessing = true
        CoroutineScope(Dispatchers.IO).launch {
            while (_shortcutQueue.isNotEmpty()) {
                val shortcut = _shortcutQueue.poll() ?: break
                try {
                    Log.d(TAG, "Processing shortcut: ${shortcut.name}")
                    updateNotification("Running ${shortcut.name}")
                    // TODO implement
                    delay(20000)
                    Log.d(TAG, "Shortcut finished: ${shortcut.name}")
                    broadcastShortcutStatue(shortcut, ShortcutState.SUCCESS)
                } catch (e: Exception) {
                    broadcastShortcutStatue(shortcut, ShortcutState.FAILURE)
                }
                updateNotification()
            }

            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            _isProcessing = false

        }
    }

    private fun createNotification(statusMessage: String? = null): Notification {
        // required for android O+
        createNotificationChannel()

        val message = statusMessage ?: when {
            _shortcutQueue.isEmpty() -> "No shortcuts in queue"
            _shortcutQueue.size == 1 -> "Processing ${_shortcutQueue.peek()?.name}"
            else -> "Processing ${_shortcutQueue.size} shortcuts"
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play_inverse_24)
            .setContentTitle("Firefly III Shortcuts")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)

        val openAppIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (openAppIntent != null) {
            openAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(
                this, 0, openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.setContentIntent(pendingIntent)
        }

        if (_shortcutQueue.isNotEmpty()) {
            val cancelIntent = Intent(this, ShortcutExecutionService::class.java).apply {
                action = ACTION_CANCEL_ALL
            }
            val cancelPendingIntent = PendingIntent.getService(
                this, 1, cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(R.drawable.baseline_close_24, "Cancel All", cancelPendingIntent)

            if (_shortcutQueue.size > 1) {
                val style = NotificationCompat.InboxStyle()
                    .setBigContentTitle("Firefly III Shortcuts")
                    .setSummaryText("${_shortcutQueue.size} shortcuts in queue")

                // Add first few shortcuts to expanded view
                _shortcutQueue.take(5).forEach { shortcut ->
                    style.addLine("• ${shortcut.name}")
                }

                builder.setStyle(style)
            }
        }
        return builder.build()
    }

    private fun createNotificationChannel() {
        // Only required for Android O and above
        val name = "Shortcut Execution"
        val descriptionText = "Shows the status of financial shortcut execution"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register channel with system
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification(message: String? = null) {
        val notification = createNotification(message)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun broadcastShortcutStatue(
        shortcutExecutionData: ShortcutExecutionData,
        state: ShortcutState
    ) {
        val intent = Intent(ACTION_SHORTCUT_STATUS_CHANGED)
        intent.putExtra(EXTRA_SHORTCUT_DATA, shortcutExecutionData)
        intent.putExtra(EXTRA_SHORTCUT_STATE, state.ordinal)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val CHANNEL_ID = "shortcut_execution_channel"
        const val NOTIFICATION_ID = 1111
        const val ACTION_SHORTCUT_STATUS_CHANGED =
            "com.mustafacanyucel.fireflyiiishortcuts.ACTION_SHORTCUT_STATUS_CHANGED"
        const val ACTION_CANCEL_ALL = "com.mustafacanyucel.fireflyiiishortcuts.ACTION_CANCEL_ALL"
        const val ACTION_SHORTCUTS_CANCELLED =
            "com.mustafacanyucel.fireflyiiishortcuts.ACTION_SHORTCUTS_CANCELLED"
        const val EXTRA_SHORTCUT_DATA = "shortcutData"
        const val EXTRA_SHORTCUT_STATE = "shortcutState"
        private const val TAG = "ShortcutExecutionService"
    }
}