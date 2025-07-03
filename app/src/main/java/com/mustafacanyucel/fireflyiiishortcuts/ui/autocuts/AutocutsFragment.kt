package com.mustafacanyucel.fireflyiiishortcuts.ui.autocuts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.databinding.FragmentAutocutsBinding
import com.mustafacanyucel.fireflyiiishortcuts.model.FragmentBase
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AutocutsFragment : FragmentBase() {

    private var _binding: FragmentAutocutsBinding? = null
    private val _viewModel: AutocutsViewModel by viewModels()
    override fun getViewModel() = _viewModel

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAutocutsBinding.inflate(inflater, container, false)

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _viewModel.autocuts.collectLatest { autocuts ->
                        // TODO: Implement recyclerview and its adapter to display autocuts

                        binding.emptyStateContainer.isVisible =
                            autocuts.isEmpty() && !_viewModel.isBusy.value

                        // TODO update recyclerview visibility
                    }
                }

                launch {
                    _viewModel.isBusy.collect { isBusy ->
                        binding.isBusy.visibility = if (isBusy) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}