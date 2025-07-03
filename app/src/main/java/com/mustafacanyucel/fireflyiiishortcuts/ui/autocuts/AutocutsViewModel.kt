package com.mustafacanyucel.fireflyiiishortcuts.ui.autocuts

import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ReferenceData
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AutocutsViewModel @Inject constructor(
    private val localFireflyRepository: LocalFireflyRepository
) : ViewModelBase() {

    private val _referenceDataFlow = MutableStateFlow<ReferenceData?>(null)
    private val _isBusy = MutableStateFlow(false)

    val isBusy = _isBusy.asStateFlow()


}