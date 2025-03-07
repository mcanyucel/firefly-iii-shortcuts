package com.mustafacanyucel.fireflyiiishortcuts.ui.management

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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.databinding.FragmentShortcutmodelListBinding
import com.mustafacanyucel.fireflyiiishortcuts.model.EventData
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import com.mustafacanyucel.fireflyiiishortcuts.model.FragmentBase
import com.mustafacanyucel.fireflyiiishortcuts.ui.listener.IShortcutClickListener
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter.ShortcutItemEditAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ShortcutModelDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@AndroidEntryPoint
class ShortcutModelListFragment : FragmentBase(), IShortcutClickListener {

    private val _viewModel: ShortcutManagementViewModel by viewModels()
    override fun getViewModel() = _viewModel
    private lateinit var _shortcutAdapter: ShortcutItemEditAdapter


    private var _binding: FragmentShortcutmodelListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortcutmodelListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupButtons()
    }


    private fun setupRecyclerView() {
        _shortcutAdapter = ShortcutItemEditAdapter(this)
        binding.shortcutmodelList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _shortcutAdapter
        }
    }

    private fun setupButtons() {
        binding.fabAddShortcut?.setOnClickListener {
            val bundle = Bundle().apply {
                putLong(ShortcutModelDetailFragment.ARG_ITEM_ID, 0L)
            }
            findNavController().navigate(R.id.show_shortcutmodel_detail, bundle)
        }
        binding.fabAddShortcutTablet?.setOnClickListener {
            val bundle = Bundle().apply {
                putLong(ShortcutModelDetailFragment.ARG_ITEM_ID, 0L)
            }
            val navHostFragment =
                childFragmentManager.findFragmentById(R.id.shortcutmodel_detail_nav_container) as? NavHostFragment
                    ?: activity?.supportFragmentManager?.findFragmentById(R.id.shortcutmodel_detail_nav_container) as? NavHostFragment

            navHostFragment?.navController?.navigate(R.id.fragment_shortcutmodel_detail, bundle)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _viewModel.shortcutListUiState.collectLatest { state ->
                        _shortcutAdapter.submitList(state.shortcuts)

                        // Update empty state visibility
                        binding.emptyStateContainer.isVisible =
                            state.shortcuts.isEmpty() && !state.isLoading
                        binding.shortcutmodelList.isVisible =
                            state.shortcuts.isNotEmpty() || state.isLoading

                        state.error?.let { error ->
                            dialogService.showDialogSnackbar(
                                EventData(EventType.ERROR, error)
                            )
                        }
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
        val bundle = Bundle().apply {
            putLong(ShortcutModelDetailFragment.ARG_ITEM_ID, shortcut.id)
        }

        val detailNavHostFragment =
            activity?.findViewById<View>(R.id.shortcutmodel_detail_nav_container)

        // tablet mode
        if (detailNavHostFragment != null) {
            try {
                val navHostFragment =
                    childFragmentManager.findFragmentById(R.id.shortcutmodel_detail_nav_container) as? NavHostFragment
                        ?: activity?.supportFragmentManager?.findFragmentById(R.id.shortcutmodel_detail_nav_container) as? NavHostFragment

                navHostFragment?.let {
                    it.navController.navigate(R.id.fragment_shortcutmodel_detail, bundle)
                    return@onShortcutClicked // Exit early if successful
                }

                // If we get here, navigation failed with the detail pane
                Log.e("Navigation", "Failed to find NavController for detail pane")
                findNavController().navigate(R.id.show_shortcutmodel_detail, bundle)
            } catch (e: Exception) {
                Log.e("Navigation", "Error navigating in tablet mode", e)
                findNavController().navigate(R.id.show_shortcutmodel_detail, bundle)
            }
        }
        // phone mode
        else {
            findNavController().navigate(R.id.show_shortcutmodel_detail, bundle)
        }
    }
}