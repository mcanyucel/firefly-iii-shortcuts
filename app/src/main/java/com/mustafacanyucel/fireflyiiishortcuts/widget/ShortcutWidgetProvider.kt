package com.mustafacanyucel.fireflyiiishortcuts.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.mustafacanyucel.fireflyiiishortcuts.MainActivity
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShortcutWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val TAG = "ShortcutWidgetProvider"
        const val ACTION_REFRESH = "com.mustafacanyucel.fireflyiiishortcuts.widget.REFRESH"
        const val ACTION_EXECUTE_SHORTCUT =
            "com.mustafacanyucel.fireflyiiishortcuts.widget.EXECUTE_SHORTCUT"
        const val EXTRA_SHORTCUT_ID = "shortcut_id"
        const val EXTRA_WIDGET_ID = "widget_id"
        const val EXTRA_SHORTCUT_STATE = "shortcut_state"

        /**
         * Update all active widgets when shortcut state changes
         */
        fun notifyShortcutStateChanged(context: Context, shortcutId: Long, state: ShortcutState) {
            val intent = Intent(context, ShortcutWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(EXTRA_SHORTCUT_ID, shortcutId)
            intent.putExtra(EXTRA_SHORTCUT_STATE, state.ordinal)

            // Find all active widget Ids
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, ShortcutWidgetProvider::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            context.sendBroadcast(intent)
        }
    }

    @Inject
    lateinit var widgetRepository: ShortcutWidgetRepository

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.d(TAG, "onUpdate: ${appWidgetIds?.joinToString()}")

        appWidgetIds?.forEach { appWidgetId ->
            updateAppWidget(context!!, appWidgetManager!!, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_REFRESH -> {
                Log.d(TAG, "onReceive: Refresh requested")
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, ShortcutWidgetProvider::class.java)
                )
                onUpdate(context, appWidgetManager, appWidgetIds)
            }

            ACTION_EXECUTE_SHORTCUT -> {
                val shortcutId = intent.getLongExtra(EXTRA_SHORTCUT_ID, -1)
                val widgetId = intent.getIntExtra(EXTRA_WIDGET_ID, -1)

                if (shortcutId != -1L && widgetId != -1) {
                    Log.d(TAG, "onReceive: Executing shortcut $shortcutId")
                    ShortcutWidgetExecutor.executeShortcut(context, shortcutId)
                }
            }

            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                // check if this is a state update notification
                if (intent.hasExtra(EXTRA_SHORTCUT_ID) && intent.hasExtra(EXTRA_SHORTCUT_STATE)) {
                    val shortcutId = intent.getLongExtra(EXTRA_SHORTCUT_ID, -1)
                    val stateOrdinal = intent.getIntExtra(EXTRA_SHORTCUT_STATE, -1)
                    val state = ShortcutState.entries[stateOrdinal]

                    Log.d(TAG, "onReceive: Shortcut state updated: $shortcutId -> $state")
                    // store the state so that it can be displayed in the list
                    widgetRepository.updateShortcutState(shortcutId, state)
                }

                // always process the standard update
                val appWidgetIds =
                    intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) ?: return
                val appWidgetManager = AppWidgetManager.getInstance(context)
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // set up the intent that starts the ShortcutWidgetService, which will provide the views
        val intent = Intent(context, ShortcutWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = toUri(Intent.URI_INTENT_SCHEME).toUri()
        }

        val views = RemoteViews(context.packageName, R.layout.shortcut_widget)
        views.setRemoteAdapter(R.id.widget_list_view, intent)
        views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view)

        // set up refresh intent
        val refreshIntent = Intent(context, ShortcutWidgetProvider::class.java).apply {
            action = ACTION_REFRESH
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context, appWidgetId, refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh_button, refreshPendingIntent)

        // set up app launch intent
        val appIntent = Intent(context, MainActivity::class.java)
        val appPendingIntent = PendingIntent.getActivity(
            context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_title, appPendingIntent)

        // Template for item click intent (will be filled in by RemoteViewsFactory
        val executeTemplate = Intent(context, ShortcutWidgetProvider::class.java).apply {
            action = ACTION_EXECUTE_SHORTCUT
            putExtra(EXTRA_WIDGET_ID, appWidgetId)
        }
        val executePendingIntentTemplate = PendingIntent.getBroadcast(
            context,
            0,
            executeTemplate,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        views.setPendingIntentTemplate(R.id.widget_list_view, executePendingIntentTemplate)

        // instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        // When the user deletes the widget, delete the preference associated with it.
        appWidgetIds?.forEach { widgetId ->
            Log.d(TAG, "onDeleted: $widgetId")
            widgetRepository.cleanupWidgetData(widgetId)
        }
    }

    override fun onEnabled(context: Context?) {
        Log.d(TAG, "First widget created")
    }

    override fun onDisabled(context: Context?) {
        Log.d(TAG, "Last widget removed")
        widgetRepository.cleanupAllWidgetData()
    }
}