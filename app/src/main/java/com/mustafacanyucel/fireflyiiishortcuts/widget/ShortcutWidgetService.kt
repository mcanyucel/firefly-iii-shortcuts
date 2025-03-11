package com.mustafacanyucel.fireflyiiishortcuts.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.data.AppDatabase
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * This is the service that provides the factory to be bound to the collection view
 */
@AndroidEntryPoint
class ShortcutWidgetService : RemoteViewsService() {

    @Inject
    lateinit var widgetRepository: ShortcutWidgetRepository

    @Inject
    lateinit var database: AppDatabase

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ShortcutWidgetItemFactory(applicationContext, intent, widgetRepository, database)
    }
}

class ShortcutWidgetItemFactory(
    private val context: Context,
    private val intent: Intent,
    private val widgetRepository: ShortcutWidgetRepository,
    private val database: AppDatabase
) : RemoteViewsService.RemoteViewsFactory {
    private val widgetId = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
    )

    private var shortcuts = listOf<ShortcutWidgetItem>()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        Log.d(TAG, "onCreate for widget $widgetId")
    }

    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged for widget $widgetId")
        try {
            // this method runs on the main thread, so we use runBlocking
            // to run db operations synchronously
            shortcuts = loadShortcutsSync()
            Log.d(TAG, "onDataSetChanged loaded ${shortcuts.size} shortcuts")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading shortcuts for widget", e)
            shortcuts = emptyList()
        }
    }

    private fun loadShortcutsSync(): List<ShortcutWidgetItem> {
        // this is a blocking operation that loads the shortcuts from the database
        val widgetItems = mutableListOf<ShortcutWidgetItem>()
        runBlocking {
            val shortcutStates = widgetRepository.getShortcutStates()
            val shortcuts = database.shortcutDao().getAllShortcutsWithTags()
            shortcuts.forEach { shortcutWithTags ->
                val shortcut = shortcutWithTags.shortcut
                val state = shortcutStates[shortcut.id] ?: ShortcutState.IDLE
                val fromAccountName = try {
                    database.accountDao().getAccountById(shortcut.fromAccountId)?.name ?: "Unknown"
                } catch (e: Exception) {
                    "Unknown"
                }
                val toAccountName = try {
                    database.accountDao().getAccountById(shortcut.toAccountId)?.name ?: "Unknown"
                } catch (e: Exception) {
                    "Unknown"
                }

                widgetItems.add(
                    ShortcutWidgetItem(
                        id = shortcut.id,
                        name = shortcut.name,
                        amount = shortcut.amount,
                        fromAccountName = fromAccountName,
                        toAccountName = toAccountName,
                        state = state
                    )
                )
            }
        }
        return widgetItems
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun getCount() = shortcuts.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position < 0 || position >= shortcuts.size) {
            return RemoteViews(context.packageName, R.layout.shortcut_widget_item)
        }

        val shortcut = shortcuts[position]
        Log.d(
            TAG,
            "getViewAt($position): Getting view for shortcut ${shortcut.id} with state ${shortcut.state}"
        )

        return RemoteViews(context.packageName, R.layout.shortcut_widget_item).apply {
            setTextViewText(R.id.widget_shortcut_name, shortcut.name)
            val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            val formattedAmount = numberFormat.format(shortcut.amount)
            setTextViewText(R.id.widget_shortcut_amount, formattedAmount)

            setTextViewText(
                R.id.widget_shortcut_accounts,
                "${shortcut.fromAccountName} -> ${shortcut.toAccountName}"
            )

            // set the play button icon based on the shortcut state
            val iconResId = when (shortcut.state) {
                ShortcutState.IDLE -> R.drawable.ic_run_48dp
                ShortcutState.QUEUED -> R.drawable.baseline_av_timer_48
                ShortcutState.RUNNING -> R.drawable.ic_sync_48dp
                ShortcutState.SUCCESS -> R.drawable.baseline_done_48
                ShortcutState.FAILURE -> R.drawable.baseline_cancel_48
            }
            setImageViewResource(R.id.widget_shortcut_execute, iconResId)

            // disable the button if the shortcut is running
            setBoolean(
                R.id.widget_shortcut_execute,
                "setEnabled",
                shortcut.state == ShortcutState.IDLE || shortcut.state == ShortcutState.FAILURE || shortcut.state == ShortcutState.SUCCESS
            )

            // set up the fill-in intent for the item
            val fillInIntent = Intent().apply {
                putExtra(ShortcutWidgetProvider.EXTRA_SHORTCUT_ID, shortcut.id)
            }
            setOnClickFillInIntent(R.id.widget_shortcut_execute, fillInIntent)
        }


    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long =
        if (position >= 0 && position < shortcuts.size) shortcuts[position].id else position.toLong()

    override fun hasStableIds(): Boolean = true

    companion object {
        private const val TAG = "ShortcutWidgetItemFactory"
    }
}