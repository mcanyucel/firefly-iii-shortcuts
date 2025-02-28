package com.mustafacanyucel.fireflyiiishortcuts.ui.sync

import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.services.sync.SyncService
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncService: SyncService
) : ViewModelBase() {

    private val _syncResult = MutableStateFlow<SyncResult?>(null)
    val syncResult = _syncResult.stateIn(viewModelScope, SharingStarted.Lazily, null)
    val syncProgress = syncService.syncProgress
    val progressPercentage = syncProgress.map { progress ->
        when (progress.step) {
            SyncStep.IDLE -> 0
            SyncStep.FETCHING_REMOTE -> 25
            SyncStep.FETCHING_LOCAL -> 50
            SyncStep.COMPARING_DATA -> 75
            SyncStep.APPLYING_CHANGES -> 90
            SyncStep.COMPLETED -> 100
            SyncStep.ERROR -> 0
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun startSync() {
        viewModelScope.launch {
            _syncResult.value = syncService.performSync()
        }
    }


}