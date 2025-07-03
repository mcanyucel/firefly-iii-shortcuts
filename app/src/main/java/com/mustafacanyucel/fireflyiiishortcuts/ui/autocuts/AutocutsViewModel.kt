package com.mustafacanyucel.fireflyiiishortcuts.ui.autocuts

import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AutocutsViewModel @Inject constructor(
    private val localFireflyRepository: LocalFireflyRepository
) : ViewModelBase() {

}