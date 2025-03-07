package com.mustafacanyucel.fireflyiiishortcuts.services.firefly

import android.app.Application
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortcutExecutionRepository @Inject constructor(
    private val application: Application,
) {
    private val _shortcutStates = MutableStateFlow<Map<Long, ShortcutState>>(emptyMap())
    val shortcutStates = _shortcutStates.asStateFlow()

    private var _serviceConnection: ServiceConnection? = null
    private var _service: ShortcutExecutionService? = null
    private var _statusReceiver: BroadcastReceiver? = null

    init {
        // setup broadcast receiver for status updates
        _statusReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == ShortcutExecutionService.ACTION_SHORTCUT_STATUS_CHANGED) {
                    val id = intent.getLongExtra(ShortcutExecutionService.EXTRA_SHORTCUT_ID, -1L)
                    val stateOrdinal =
                        intent.getIntExtra(ShortcutExecutionService.EXTRA_SHORTCUT_STATE, 0)
                    val state = ShortcutState.entries[stateOrdinal]

                    updateShortcutState(id, state)
                }
            }
        }

        LocalBroadcastManager.getInstance(application).registerReceiver(
            _statusReceiver!!,
            IntentFilter(ShortcutExecutionService.ACTION_SHORTCUT_STATUS_CHANGED)
        )

        connectToService()
    }

    private fun connectToService() {
        _serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                _service = (binder as ShortcutExecutionService.LocalBinder).getService()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                _service = null
                // try reconnecting
                connectToService()
            }
        }

        val intent = Intent(application, ShortcutExecutionService::class.java)
        application.bindService(intent, _serviceConnection!!, Context.BIND_AUTO_CREATE)
    }

    fun executeShortcut(shortcut: ShortcutModel) {
        Log.d(TAG, "executeShortcut: $shortcut")
        updateShortcutState(shortcut.id, ShortcutState.QUEUED)

        // Try to use bound service
        if (_service != null) {
            Log.d(TAG, "executeShortcut: using bound service")
            _service?.enqueueShortcut(shortcut)
        } else {
            // fallback to start service
            Log.d(TAG, "executeShortcut: fallback to start service")
            Intent(application, ShortcutExecutionService::class.java).apply {
                putExtra(ShortcutExecutionService.EXTRA_SHORTCUT_ID, shortcut.id)
            }.let {
                application.startForegroundService(it)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateShortcutState(id: Long, state: ShortcutState) {
        _shortcutStates.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            newMap[id] = state

            // auto-reset completed & failed states after a delay
            if (state == ShortcutState.SUCCESS || state == ShortcutState.FAILURE) {
                GlobalScope.launch {
                    delay(5000)
                    _shortcutStates.update { current ->
                        current.toMutableMap().apply {
                            this[id] = ShortcutState.IDLE
                        }
                    }
                }

            }
            newMap
        }
    }

    fun cleanup() {
        _serviceConnection?.let {
            application.unbindService(it)
            _serviceConnection = null
        }
        _statusReceiver?.let {
            LocalBroadcastManager.getInstance(application).unregisterReceiver(it)
            _statusReceiver = null
        }
    }

    companion object {
        private const val TAG = "ShortcutExecutionRepository"
    }
}