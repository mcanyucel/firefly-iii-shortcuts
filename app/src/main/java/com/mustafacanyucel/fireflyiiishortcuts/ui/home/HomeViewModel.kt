package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ReferenceData
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localFireflyRepository: LocalFireflyRepository
) : ViewModelBase() {

    private val _referenceDataFlow = MutableStateFlow<ReferenceData?>(null)
    private val _shortcutsWithTagsFlow = localFireflyRepository.observeAllShortcutsWithTags()

    val uiState: StateFlow<UiState> = combine(
        _shortcutsWithTagsFlow,
        _referenceDataFlow
    ) { shortcuts, refData ->
        if (refData == null) {
            UiState(isLoading = true)
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
                ShortcutModel.fromEntity(
                    shortcutWithTags,
                    fromAccount,
                    toAccount,
                    category,
                    budget,
                    bill,
                    piggybank
                )
            }
            UiState(
                isLoading = false,
                shortcuts = shortcutModels
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(500),
            initialValue = UiState(isLoading = true)
        )

}