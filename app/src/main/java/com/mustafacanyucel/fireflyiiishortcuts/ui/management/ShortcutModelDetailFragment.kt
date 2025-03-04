package com.mustafacanyucel.fireflyiiishortcuts.ui.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mustafacanyucel.fireflyiiishortcuts.R
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.databinding.FragmentShortcutmodelDetailBinding
import com.mustafacanyucel.fireflyiiishortcuts.model.EventData
import com.mustafacanyucel.fireflyiiishortcuts.model.EventType
import com.mustafacanyucel.fireflyiiishortcuts.model.FragmentBase
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter.AccountSpinnerAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter.BillSpinnerAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter.BudgetSpinnerAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter.CategorySpinnerAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter.PiggybankSpinnerAdapter
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutDetailUiState
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.model.ShortcutEntityDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A fragment representing a single ShortcutModel detail screen.
 * This fragment is either contained in a [ShortcutModelListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class ShortcutModelDetailFragment : FragmentBase() {


    private val _viewModel: ShortcutManagementViewModel by viewModels()
    private var toolbarLayout: CollapsingToolbarLayout? = null
    private lateinit var selectedTagIds: MutableList<String>

    private var _binding: FragmentShortcutmodelDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun getViewModel() = _viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                val itemId = it.getLong(ARG_ITEM_ID)
                _viewModel.setShortcutById(itemId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShortcutmodelDetailBinding.inflate(inflater, container, false)

        setupObservers()
        setupTagSelector()
        setupButtons()
        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _viewModel.shortcutDetailUiState.collectLatest { state ->
                        updateContent(state)
                        setupTagSelector(state)
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        binding.fab.setOnClickListener {
            val shortcutDTO = generateShortcutDTO() ?: return@setOnClickListener
            _viewModel.saveShortcut(shortcutDTO)
        }
        binding.btnDelete.setOnClickListener {
            _viewModel.deleteShortcut()
        }
    }

    private fun generateShortcutDTO(): ShortcutEntityDTO? {
        val name = binding.inputLayoutShortcutName.editText?.text.toString()
        val amount = binding.editTextAmount.text.toString()
        val fromAccountId = (binding.spinnerFromAccount.selectedItem as? AccountEntity)?.id
        val toAccountId = (binding.spinnerToAccount.selectedItem as? AccountEntity)?.id
        val categoryId = (binding.spinnerCategory.selectedItem as? CategoryEntity)?.id
        val budgetId = (binding.spinnerBudget.selectedItem as? BudgetEntity)?.id
        val billId = (binding.spinnerBill.selectedItem as? BillEntity)?.id
        val piggybankId = (binding.spinnerPiggybank.selectedItem as? PiggybankEntity)?.id
        val tagIds = selectedTagIds.toList()

        if (name.isEmpty() || amount.isEmpty()) {
            dialogService.showDialogSnackbar(
                EventData(
                    EventType.ERROR,
                    "Name and amount are required."
                )
            )
            return null
        }

        if (fromAccountId == null || toAccountId == null) {
            dialogService.showDialogSnackbar(
                EventData(
                    EventType.ERROR,
                    "From and to accounts are required."
                )
            )
            return null
        }

        return ShortcutEntityDTO(
            name = name,
            amount = amount.toBigDecimal(),
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            categoryId = categoryId,
            budgetId = budgetId,
            billId = billId,
            piggybankId = piggybankId,
            tagIds = tagIds
        )
    }

    private fun setupTagSelector(shortcutDetailUiState: ShortcutDetailUiState? = null) {
        val allTags = shortcutDetailUiState?.tags ?: emptyList()
        selectedTagIds = (shortcutDetailUiState?.draftShortcut?.tagEntities?.map { it.id }
            ?: emptyList()).toMutableList()
        binding.chipGroupTags.removeAllViews()

        val tagAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            allTags.map { it.tag }
        )

        binding.autocompleteTags.setAdapter(tagAdapter)

        selectedTagIds.forEach { tagId ->
            val tag = allTags.find { it.id == tagId }
            tag?.let { addTagChip(it) }
        }

        binding.autocompleteTags.setOnItemClickListener { parent, _, position, _ ->
            val selectedTagName = parent.getItemAtPosition(position).toString()
            val selectedTag = allTags.find { it.tag == selectedTagName }
            selectedTag?.let { addTag(it) }
            binding.autocompleteTags.setText("")
        }

        binding.btnShowAllTags.setOnClickListener {
            showTagSelectionDialog(allTags)
        }
    }

    private fun addTagChip(tag: TagEntity) {
        val chip = Chip(requireContext()).apply {
            text = tag.tag
            isCloseIconVisible = true
            this.tag = tag.id

            setOnCloseIconClickListener {
                binding.chipGroupTags.removeView(this)
                selectedTagIds.remove(tag.id)
            }
        }
        binding.chipGroupTags.addView(chip)
    }


    private fun addTag(tag: TagEntity) {
        if (!selectedTagIds.contains(tag.id)) {
            selectedTagIds.add(tag.id)
            addTagChip(tag)
        }
    }

    private fun removeTag(tag: TagEntity) {
        if (selectedTagIds.contains(tag.id)) {
            selectedTagIds.remove(tag.id)
            binding.chipGroupTags.children.find { (it as Chip).tag == tag.id }?.let {
                binding.chipGroupTags.removeView(it)
            }
        }
    }

    private fun showTagSelectionDialog(allTags: List<TagEntity>) {
        val tagNames = allTags.map { it.tag }.toTypedArray()
        val checkedItems = allTags.map { selectedTagIds.contains(it.id) }.toBooleanArray()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.select_tags)
            .setMultiChoiceItems(tagNames, checkedItems) { _, position, isChecked ->
                checkedItems[position] = isChecked
            }
            .setPositiveButton(R.string.confirm) { _, _ ->
                allTags.forEachIndexed { index, tagEntity ->
                    if (checkedItems[index]) {
                        addTag(tagEntity)
                    } else {
                        removeTag(tagEntity)
                    }
                }
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateContent(shortcutDetailUiState: ShortcutDetailUiState) {
        toolbarLayout?.title = "Add/Edit Shortcut"

        binding.inputLayoutShortcutName.editText?.setText(
            shortcutDetailUiState.draftShortcut?.name ?: "New Shortcut"
        )
        binding.editTextAmount.setText(shortcutDetailUiState.draftShortcut?.amount?.toPlainString())

        setupSpinners(shortcutDetailUiState)
        setupTagSelector(shortcutDetailUiState)
    }

    private fun setupSpinners(state: ShortcutDetailUiState) {
        val fromAccountAdapter = AccountSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + state.accounts
        )
        fromAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFromAccount.adapter = fromAccountAdapter
        state.draftShortcut?.fromAccountEntity?.id?.let { fromAccountId ->
            val fromAccountPosition = fromAccountAdapter.getPositionForAccountId(fromAccountId)
            binding.spinnerFromAccount.setSelection(fromAccountPosition, false)
        }

        val toAccountAdapter = AccountSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + state.accounts
        )
        toAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerToAccount.adapter = toAccountAdapter
        state.draftShortcut?.toAccountEntity?.id?.let { toAccountId ->
            val toAccountPosition = toAccountAdapter.getPositionForAccountId(toAccountId)
            binding.spinnerToAccount.setSelection(toAccountPosition, false)
        }

        val categoryAdapter = CategorySpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + state.categories
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter
        state.draftShortcut?.categoryEntity?.id?.let { categoryId ->
            val categoryPosition = categoryAdapter.getPositionForCategoryId(categoryId)
            binding.spinnerCategory.setSelection(categoryPosition, false)
        }

        val budgetAdapter = BudgetSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + state.budgets
        )
        budgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBudget.adapter = budgetAdapter
        state.draftShortcut?.budgetEntity?.id?.let { budgetId ->
            val budgetPosition = budgetAdapter.getPositionForBudgetId(budgetId)
            binding.spinnerBudget.setSelection(budgetPosition, false)
        }

        val billAdapter = BillSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + state.bills
        )
        billAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBill.adapter = billAdapter
        state.draftShortcut?.billEntity?.id?.let { billId ->
            val billPosition = billAdapter.getPositionForBillId(billId)
            binding.spinnerBill.setSelection(billPosition, false)
        }

        val piggybankAdapter = PiggybankSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + state.piggybanks
        )
        piggybankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPiggybank.adapter = piggybankAdapter
        state.draftShortcut?.piggybankEntity?.id?.let { piggybankId ->
            val piggybankPosition = piggybankAdapter.getPositionForPiggybankId(piggybankId)
            binding.spinnerPiggybank.setSelection(piggybankPosition, false)
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}