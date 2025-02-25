package com.mustafacanyucel.fireflyiiishortcuts.model

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mustafacanyucel.fireflyiiishortcuts.services.dialog.IDialogService
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class FragmentBase : Fragment() {
    @Inject lateinit var dialogService: IDialogService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewModel()?.let { viewModelBase ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModelBase.errorEvent.collect { event ->
                    dialogService.showErrorSnackbar(event.message, event.action)
                }
            }
        }
    }

    open fun getViewModel(): ViewModelBase? = null
}