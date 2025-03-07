package com.mustafacanyucel.fireflyiiishortcuts.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mustafacanyucel.fireflyiiishortcuts.databinding.FragmentHomeBinding
import com.mustafacanyucel.fireflyiiishortcuts.model.FragmentBase
import com.mustafacanyucel.fireflyiiishortcuts.ui.listener.IShortcutClickListener
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : FragmentBase(), IShortcutClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val _viewModel: HomeViewModel by viewModels()
    private val _shortcutListAdapter = ShortcutItemDisplayAdapter(this)
    override fun getViewModel() = _viewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.shortcutmodelList.adapter = _shortcutListAdapter

        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _viewModel.uiState.collectLatest { state ->
                        _shortcutListAdapter.submitList(state.shortcuts)

                        // update empty state visibility
                        binding.emptyStateContainer.isVisible =
                            state.shortcuts.isEmpty() && !state.isBusy
                        binding.shortcutmodelList.isVisible =
                            state.shortcuts.isNotEmpty() || state.isBusy

                        binding.isBusy.visibility = if (state.isBusy) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    _viewModel.isBusy.collectLatest { isBusy ->
                        binding.isBusy.visibility = if (isBusy) View.VISIBLE else View.GONE
                        binding.shortcutmodelList.isEnabled = !isBusy
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onShortcutClicked(shortcut: ShortcutModel) {
        _viewModel.runShortcut(shortcut)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}