package com.mustafacanyucel.fireflyiiishortcuts.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mustafacanyucel.fireflyiiishortcuts.databinding.FragmentSyncBinding
import com.mustafacanyucel.fireflyiiishortcuts.model.FragmentBase
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [SyncFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SyncFragment : FragmentBase() {

    private val _viewModel: SyncViewModel by viewModels()
    override fun getViewModel() = _viewModel

    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSyncBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = _viewModel

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SyncFragment.
         */
        @JvmStatic
        fun newInstance() = SyncFragment()
    }
}