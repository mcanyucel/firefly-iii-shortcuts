package com.mustafacanyucel.fireflyiiishortcuts.ui.management

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ReferenceData
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutDetailUiState
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutEntityDTO
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutListUiState
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortcutManagementViewModel @Inject constructor(
    private val localFireflyRepository: LocalFireflyRepository
) : ViewModelBase() {

    private var _selectedShortcutIdFlow = MutableStateFlow<Long?>(null)
    private val _referenceDataFlow = MutableStateFlow<ReferenceData?>(null)
    private var _lastDeletedShortcutId: Long = -1L

    private val _shortcutsWithTagsFlow = localFireflyRepository.observeAllShortcutsWithTags()

    val shortcutListUiState: StateFlow<ShortcutListUiState> = combine(
        _shortcutsWithTagsFlow,
        _referenceDataFlow
    ) { shortcuts, refData ->
        if (refData == null) {
            ShortcutListUiState(isLoading = true)
        } else {
            val shortcutModels = shortcuts.map { shortcutWithTags ->

                val selectedShortcutFromAccount =
                    refData.accounts.find { it.id == shortcutWithTags.shortcut.fromAccountId }
                val selectedShortcutToAccount =
                    refData.accounts.find { it.id == shortcutWithTags.shortcut.toAccountId }
                val selectedShortcutCategory =
                    refData.categories.find { it.id == shortcutWithTags.shortcut.categoryId }
                val selectedShortcutBudget =
                    refData.budgets.find { it.id == shortcutWithTags.shortcut.budgetId }
                val selectedShortcutBill =
                    refData.bills.find { it.id == shortcutWithTags.shortcut.billId }
                val selectedShortcutPiggybank =
                    refData.piggybanks.find { it.id == shortcutWithTags.shortcut.piggybankId }
                ShortcutModel.fromEntity(
                    shortcutWithTags,
                    selectedShortcutFromAccount,
                    selectedShortcutToAccount,
                    selectedShortcutCategory,
                    selectedShortcutBudget,
                    selectedShortcutBill,
                    selectedShortcutPiggybank,
                )
            }
            ShortcutListUiState(
                shortcuts = shortcutModels,
                isLoading = false
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = ShortcutListUiState(isLoading = true)
        )

    val shortcutDetailUiState: StateFlow<ShortcutDetailUiState> = combine(
        _shortcutsWithTagsFlow,
        _selectedShortcutIdFlow,
        _referenceDataFlow,
    ) { shortcuts, selectedShortcutId, refData ->
        if (refData == null || selectedShortcutId == null) {
            ShortcutDetailUiState(isLoading = true)
        }
        else if (selectedShortcutId == _lastDeletedShortcutId) {
            _selectedShortcutIdFlow.value = 0L // this will trigger a refresh
            _lastDeletedShortcutId = -1L       // reset the last deleted shortcut id
            ShortcutDetailUiState(isLoading = true)
        }
         else if (selectedShortcutId == 0L) {
            ShortcutDetailUiState(
                draftShortcut = ShortcutModel.createNew(),
                isLoading = false,
                accounts = refData.accounts,
                categories = refData.categories,
                budgets = refData.budgets,
                bills = refData.bills,
                piggybanks = refData.piggybanks,
                tags = refData.tags
            )
        } else if (selectedShortcutId != 0L) {
            val selectedShortcutWithTags = shortcuts.find { it.shortcut.id == selectedShortcutId }
            val selectedShortcutFromAccount =
                refData.accounts.find { it.id == selectedShortcutWithTags?.shortcut?.fromAccountId }
            val selectedShortcutToAccount =
                refData.accounts.find { it.id == selectedShortcutWithTags?.shortcut?.toAccountId }
            val selectedShortcutCategory =
                refData.categories.find { it.id == selectedShortcutWithTags?.shortcut?.categoryId }
            val selectedShortcutBudget =
                refData.budgets.find { it.id == selectedShortcutWithTags?.shortcut?.budgetId }
            val selectedShortcutBill =
                refData.bills.find { it.id == selectedShortcutWithTags?.shortcut?.billId }
            val selectedShortcutPiggybank =
                refData.piggybanks.find { it.id == selectedShortcutWithTags?.shortcut?.piggybankId }
            val model = ShortcutModel.fromEntity(
                selectedShortcutWithTags ?: throw IllegalStateException("Draft shortcut not found"),
                selectedShortcutFromAccount,
                selectedShortcutToAccount,
                selectedShortcutCategory,
                selectedShortcutBudget,
                selectedShortcutBill,
                selectedShortcutPiggybank
            )
            ShortcutDetailUiState(
                draftShortcut = model,
                isLoading = false,
                accounts = refData.accounts,
                categories = refData.categories,
                budgets = refData.budgets,
                bills = refData.bills,
                piggybanks = refData.piggybanks,
                tags = refData.tags
            )
        } else {
            throw IllegalStateException("Draft shortcut must have an ID")
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = ShortcutDetailUiState(isLoading = true)
    )

    fun setShortcutById(shortcutId: Long) {
        _selectedShortcutIdFlow.value = shortcutId
    }

    /**
     * Save a shortcut entity and its associated tags
     * Works for both new shortcuts (from draft) and existing ones
     */
    fun saveShortcut(shortcutEntityDTO: ShortcutEntityDTO) {
        if (_selectedShortcutIdFlow.value == null) {
            throw IllegalStateException("Shortcut must have an ID")
        }
        viewModelScope.launch {
            try {
                val entity = shortcutEntityDTO.toEntity(_selectedShortcutIdFlow.value!!)
                localFireflyRepository.saveShortcutWithTags(entity, shortcutEntityDTO.tagIds)
                emitEvent(EventType.SUCCESS, "Shortcut saved successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save shortcut", e)
                emitEvent(EventType.ERROR, "Failed to save shortcut")
            }
        }
    }

    fun deleteShortcut() {
        if (_selectedShortcutIdFlow.value == null || _selectedShortcutIdFlow.value == 0L) {
            emitEvent(EventType.ERROR, "This shortcut is not saved yet")
        } else {
            viewModelScope.launch {
                try {
                    _lastDeletedShortcutId = _selectedShortcutIdFlow.value!!
                    localFireflyRepository.deleteTagsForShortcut(_selectedShortcutIdFlow.value!!)
                    localFireflyRepository.deleteShortcut(_selectedShortcutIdFlow.value!!)
                    emitEvent(EventType.SUCCESS, "Shortcut deleted successfully")
                } catch (e: Exception) {
                    _lastDeletedShortcutId = -1L
                    Log.e(TAG, "Failed to delete shortcut", e)
                }
            }
        }
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

    fun refreshReferenceData() = loadReferenceData()

    init {
        loadReferenceData()
    }

    companion object {
        private const val TAG = "ShortcutManagementViewModel"
    }
}