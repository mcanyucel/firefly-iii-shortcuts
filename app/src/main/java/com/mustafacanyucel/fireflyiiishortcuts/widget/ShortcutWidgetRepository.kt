package com.mustafacanyucel.fireflyiiishortcuts.widget

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.edit
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortcutWidgetRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * In-memory cache of shortcut states to avoid frequent disk reads
     */
    private val shortcutStates = mutableMapOf<Long, ShortcutState>()

    /**
     * Map to store handlers for state reset operations
     */
    private val resetHandlers = ConcurrentHashMap<Long, Handler>()

    init {
        loadStatesFromPrefs()
    }

    private fun loadStatesFromPrefs() {
        synchronized(shortcutStates) {
            shortcutStates.clear()

            prefs.all.forEach { (key, value) ->
                if (key.startsWith(KEY_STATE_PREFIX) && value is Int) {
                    try {
                        val shortcutId = key.removePrefix(KEY_STATE_PREFIX).toLong()
                        val state = ShortcutState.entries[value]
                        shortcutStates[shortcutId] = state
                    } catch (e: Exception) {
                        Log.e(TAG, "Error loading shortcut state from preferences", e)
                    }
                }
            }

            Log.d(TAG, "Loaded ${shortcutStates.size} shortcut states from preferences")
        }
    }

    fun getShortcutState(shortcutId: Long): ShortcutState {
        return synchronized(shortcutStates) {
            shortcutStates[shortcutId] ?: ShortcutState.IDLE
        }
    }

    fun getShortcutStates(): Map<Long, ShortcutState> {
        return synchronized(shortcutStates) {
            shortcutStates.toMap()
        }
    }

    fun updateShortcutState(shortcutId: Long, state: ShortcutState) {
        synchronized(shortcutStates) {
            shortcutStates[shortcutId] = state

            prefs.edit { putInt("$KEY_STATE_PREFIX$shortcutId", state.ordinal) }
            Log.d(TAG, "Updated shortcut state for $shortcutId to $state")
            // if the state is SUCCESS or FAILURE, reset after a delay
            if (state == ShortcutState.SUCCESS || state == ShortcutState.FAILURE) {
                scheduleStateReset(shortcutId)
            } else if (state == ShortcutState.IDLE) {
                cancelScheduleReset(shortcutId)
            }
        }
    }

    /**
     * Clean up data for a specific widget
     */
    fun cleanupWidgetData(widgetId: Int) {
        // widget specific data cleanup if needed
    }

    /**
     * Clean up all widget data
     */
    fun cleanupAllWidgetData() {
        synchronized(shortcutStates) {
            shortcutStates.clear()
            prefs.edit { clear() }
            resetHandlers.forEach { (_, handler) ->
                handler.removeCallbacksAndMessages(null)
            }
            resetHandlers.clear()
            Log.d(TAG, "Cleaned up all widget data")
        }
    }

    /**
     * Schedule a state reset after a delay
     */
    private fun scheduleStateReset(shortcutId: Long) {
        cancelScheduleReset(shortcutId)

        val handler = Handler(Looper.getMainLooper())
        resetHandlers[shortcutId] = handler

        handler.postDelayed({
            try {
                Log.d(TAG, "Auto-resetting shortcut state for $shortcutId")
                updateShortcutState(shortcutId, ShortcutState.IDLE)

                ShortcutWidgetProvider.notifyShortcutStateChanged(
                    context,
                    shortcutId,
                    ShortcutState.IDLE
                )
                resetHandlers.remove(shortcutId)
            } catch (e: Exception) {
                Log.e(TAG, "Error auto-resetting shortcut state for $shortcutId", e)
            }

        }, RESET_DELAY_MS)

        Log.d(TAG, "Scheduled auto-reset for shortcut $shortcutId in $RESET_DELAY_MS ms")
    }

    private fun cancelScheduleReset(shortcutId: Long) {
        resetHandlers[shortcutId]?.let { handler ->
            handler.removeCallbacksAndMessages(null)
            resetHandlers.remove(shortcutId)
            Log.d(TAG, "Cancelled auto-reset for shortcut $shortcutId")
        }
    }

    companion object {
        private const val TAG = "ShortcutWidgetRepo"
        private const val PREFS_NAME = "widget_preferences"
        private const val KEY_STATE_PREFIX = "shortcut_state_"
        private const val RESET_DELAY_MS = 5000L
    }
}