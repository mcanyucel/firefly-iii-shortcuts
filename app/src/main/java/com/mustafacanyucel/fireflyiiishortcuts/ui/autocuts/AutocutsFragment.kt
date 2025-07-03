package com.mustafacanyucel.fireflyiiishortcuts.ui.autocuts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.databinding.FragmentAutocutsBinding
import com.mustafacanyucel.fireflyiiishortcuts.model.FragmentBase
import com.mustafacanyucel.fireflyiiishortcuts.vm.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint


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

    }
}