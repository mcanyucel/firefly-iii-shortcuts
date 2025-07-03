package com.mustafacanyucel.fireflyiiishortcuts.ui.autocuts

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ReferenceData
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutocutsViewModel @Inject constructor(
    private val localFireflyRepository: LocalFireflyRepository
) : ViewModelBase() {

    private val _isBusy = MutableStateFlow(false)
    private val _autocuts = MutableStateFlow<List<AutocutEntity>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)

    val isBusy = _isBusy.asStateFlow()
    val autocuts: StateFlow<List<AutocutEntity>> = _autocuts.asStateFlow()
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAutocuts()
    }

    private fun loadAutocuts() {
        viewModelScope.launch {
            try {
                clearError()

                localFireflyRepository.observeAllAutocuts().collect { autocutList ->
                    _autocuts.value = autocutList
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load autocuts"
                Log.e("AutocutsViewModel", "Failed to load autocuts", e)
            }
        }
    }

    fun deleteAutocut(autocut: AutocutEntity) {
        viewModelScope.launch {
            try {
                _isBusy.value = true
                clearError()
                localFireflyRepository.deleteAutocut(autocut)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete autocut"
            } finally {
                _isBusy.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }


}