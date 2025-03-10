package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import com.mustafacanyucel.fireflyiiishortcuts.services.firefly.ShortcutExecutionRepository
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ReferenceData
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localFireflyRepository: LocalFireflyRepository,
    private val shortcutExecutionRepository: ShortcutExecutionRepository
) : ViewModelBase() {

    private val _referenceDataFlow = MutableStateFlow<ReferenceData?>(null)
    private val _shortcutsWithTagsFlow = localFireflyRepository.observeAllShortcutsWithTags()
    private val _isBusy = MutableStateFlow(false)
    private val _shortcutStates = shortcutExecutionRepository.shortcutStates

    val isBusy = _isBusy.asStateFlow()
    val stateMap get() = _shortcutStates

    val uiState: StateFlow<UiState> = combine(
        _shortcutsWithTagsFlow,
        _referenceDataFlow,
        _shortcutStates
    ) { shortcuts, refData, shortcutStates ->
        if (refData == null) {
            UiState(isBusy = true)
        } else {
            val shortcutModels = shortcuts.map { shortcutWithTags ->
                val fromAccount =
                    refData.accounts.find { it.id == shortcutWithTags.shortcut.fromAccountId }
                val toAccount =
                    refData.accounts.find { it.id == shortcutWithTags.shortcut.toAccountId }
                val category =
                    refData.categories.find { it.id == shortcutWithTags.shortcut.categoryId }
                val budget =
                    refData.budgets.find { it.id == shortcutWithTags.shortcut.budgetId }
                val bill = refData.bills.find { it.id == shortcutWithTags.shortcut.billId }
                val piggybank =
                    refData.piggybanks.find { it.id == shortcutWithTags.shortcut.piggybankId }
                val shortcutState = shortcutStates[shortcutWithTags.shortcut.id] ?: ShortcutState.IDLE
                val shortcutModel = ShortcutModel.fromEntity(
                    shortcutWithTags,
                    fromAccount,
                    toAccount,
                    category,
                    budget,
                    bill,
                    piggybank
                )
                ShortcutWithState(
                    shortcut = shortcutModel,
                    state = shortcutState
                )
            }
            UiState(
                isBusy = false,
                shortcuts = shortcutModels
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(500),
            initialValue = UiState(isBusy = true)
        )

    fun executeShortcut(shortcut: ShortcutModel) {
        Log.d(TAG, "Executing shortcut: $shortcut")
        shortcutExecutionRepository.executeShortcut(shortcut)
    }

    private fun loadReferenceData() {
        viewModelScope.launch {
            try {
                // Load all reference data in parallel
                val accounts = async { localFireflyRepository.getAllAccounts() }
                val categories = async { localFireflyRepository.getAllCategories() }
                val budgets = async { localFireflyRepository.getAllBudgets() }
                val bills = async { localFireflyRepository.getAllBills() }
                val tags = async { localFireflyRepository.getAllTags() }
                val piggybanks = async { localFireflyRepository.getAllPiggybanks() }

                _referenceDataFlow.value = ReferenceData(
                    accounts = accounts.await(),
                    categories = categories.await(),
                    budgets = budgets.await(),
                    bills = bills.await(),
                    tags = tags.await(),
                    piggybanks = piggybanks.await()
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load reference data", e)
                emitEvent(EventType.ERROR, "Failed to load reference data")
            }
        }
    }

    init {
        loadReferenceData()
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}