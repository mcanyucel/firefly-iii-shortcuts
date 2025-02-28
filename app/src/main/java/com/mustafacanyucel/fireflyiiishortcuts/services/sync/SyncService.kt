package com.mustafacanyucel.fireflyiiishortcuts.services.sync

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.IEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.ApiResult
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.model.ComparisonResult
import com.mustafacanyucel.fireflyiiishortcuts.ui.sync.DbChangeResult
import com.mustafacanyucel.fireflyiiishortcuts.ui.sync.SyncProgress
import com.mustafacanyucel.fireflyiiishortcuts.ui.sync.SyncResult
import com.mustafacanyucel.fireflyiiishortcuts.ui.sync.SyncStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.reflect.KClass

class SyncService @Inject constructor(
    private val remoteFireflyRepository: RemoteFireflyRepository,
    private val localFireflyRepository: LocalFireflyRepository
) {

    private var _remoteCategories: List<CategoryEntity>? = null
    private var _remoteAccounts: List<AccountEntity>? = null
    private var _remoteBills: List<BillEntity>? = null
    private var _remoteBudgets: List<BudgetEntity>? = null
    private var _remotePiggybanks: List<PiggybankEntity>? = null
    private var _remoteTags: List<TagEntity>? = null

    private var _localCategories: List<CategoryEntity>? = null
    private var _localAccounts: List<AccountEntity>? = null
    private var _localBills: List<BillEntity>? = null
    private var _localBudgets: List<BudgetEntity>? = null
    private var _localPiggyBanks: List<PiggybankEntity>? = null
    private var _localTags: List<TagEntity>? = null

    private var _entityMaps = hashMapOf<KClass<out IEntity>, EntityLists<IEntity>>()

    private var _syncProgress = MutableStateFlow(SyncProgress(SyncStep.IDLE, "Ready to sync"))

    val syncProgress = _syncProgress.asStateFlow()

    suspend fun performSync(): SyncResult? {
        try {
            _syncProgress.value = SyncProgress(SyncStep.FETCHING_REMOTE, "Fetching remote data")
            fetchRemoteData()
            if (!verifyFetchedData()) {
                _syncProgress.value = SyncProgress(
                    SyncStep.ERROR,
                    "Failed to fetch remote data",
                    "At least one entity type failed to fetch"
                )
                return null
            }

            _syncProgress.value = SyncProgress(SyncStep.FETCHING_LOCAL, "Fetching local data")
            fetchLocalData()
            if (!verifyLocalData()) {
                _syncProgress.value = SyncProgress(
                    SyncStep.ERROR,
                    "Failed to fetch local data",
                    "At least one entity type failed to fetch"
                )
                return null
            }

            _syncProgress.value = SyncProgress(SyncStep.COMPARING_DATA, "Comparing data")
            val comparisonResult = compareData()

            _syncProgress.value = SyncProgress(SyncStep.APPLYING_CHANGES, "Applying changes")
            val syncResult = applyChanges(comparisonResult)
            _syncProgress.value = SyncProgress(SyncStep.COMPLETED, "Sync completed successfully.")
            return syncResult
        } catch (e: Exception) {
            Log.e(TAG, "Error during sync", e)
            _syncProgress.value =
                SyncProgress(SyncStep.ERROR, "Sync failed", e.message ?: "Unknown error")
            return null
        }
    }

    private suspend fun applyAccountChanges(comparisonResult: ComparisonResult<IEntity>): DbChangeResult {
        @Suppress("UNCHECKED_CAST")
        val added = comparisonResult.added as List<AccountEntity>
        added.forEach { localFireflyRepository.insertAccount(it) }

        @Suppress("UNCHECKED_CAST")
        val updated = comparisonResult.updated as List<AccountEntity>
        updated.forEach { localFireflyRepository.updateAccount(it) }


        @Suppress("UNCHECKED_CAST")
        val removed = comparisonResult.removed as List<AccountEntity>
        var deletedShortcutsCount = 0

        for (account in removed) {
            val fromShortcuts = localFireflyRepository.getShortcutsByFromAccountId(account.id)
            val toShortcuts = localFireflyRepository.getShortcutsByToAccountId(account.id)
            val affectedShortcuts = (fromShortcuts + toShortcuts).distinctBy { it.id }
            affectedShortcuts.forEach { localFireflyRepository.deleteShortcut(it.id) } // delete all shortcuts that reference this account
            deletedShortcutsCount += affectedShortcuts.size
            localFireflyRepository.deleteAccount(account.id)
        }

        return DbChangeResult(added.size, updated.size, removed.size, deletedShortcutsCount)
    }

    private suspend fun applyCategoryChanges(comparisonResult: ComparisonResult<IEntity>): DbChangeResult {
        @Suppress("UNCHECKED_CAST")
        val added = comparisonResult.added as List<CategoryEntity>
        added.forEach { localFireflyRepository.insertCategory(it) }

        @Suppress("UNCHECKED_CAST")
        val updated = comparisonResult.updated as List<CategoryEntity>
        updated.forEach { localFireflyRepository.updateCategory(it) }


        @Suppress("UNCHECKED_CAST")
        val removed = comparisonResult.removed as List<CategoryEntity>
        var affectedShortcutsCount = 0

        for (category in removed) {
            val shortcuts = localFireflyRepository.getShortcutsByCategoryId(category.id)
            affectedShortcutsCount += shortcuts.size
            // room should set the foreign keys to NULL for us.
            localFireflyRepository.deleteCategory(category.id)
        }

        return DbChangeResult(added.size, updated.size, removed.size, affectedShortcutsCount)
    }

    private suspend fun applyBudgetChanges(comparisonResult: ComparisonResult<IEntity>): DbChangeResult {
        @Suppress("UNCHECKED_CAST")
        val added = comparisonResult.added as List<BudgetEntity>
        added.forEach { localFireflyRepository.insertBudget(it) }

        @Suppress("UNCHECKED_CAST")
        val updated = comparisonResult.updated as List<BudgetEntity>
        updated.forEach { localFireflyRepository.updateBudget(it) }


        @Suppress("UNCHECKED_CAST")
        val removed = comparisonResult.removed as List<BudgetEntity>
        var affectedShortcutsCount = 0

        for (budget in removed) {
            val shortcuts = localFireflyRepository.getShortcutsByBudgetId(budget.id)
            affectedShortcutsCount += shortcuts.size
            // room should set the foreign keys to NULL for us.
            localFireflyRepository.deleteBudget(budget.id)
        }

        return DbChangeResult(added.size, updated.size, removed.size, affectedShortcutsCount)
    }

    private suspend fun applyBillChanges(comparisonResult: ComparisonResult<IEntity>): DbChangeResult {
        @Suppress("UNCHECKED_CAST")
        val added = comparisonResult.added as List<BillEntity>
        added.forEach { localFireflyRepository.insertBill(it) }

        @Suppress("UNCHECKED_CAST")
        val updated = comparisonResult.updated as List<BillEntity>
        updated.forEach { localFireflyRepository.updateBill(it) }


        @Suppress("UNCHECKED_CAST")
        val removed = comparisonResult.removed as List<BillEntity>
        var affectedShortcutsCount = 0

        for (bill in removed) {
            val shortcuts = localFireflyRepository.getShortcutsByBillId(bill.id)
            affectedShortcutsCount += shortcuts.size
            // room should set the foreign keys to NULL for us.
            localFireflyRepository.deleteBill(bill.id)
        }

        return DbChangeResult(added.size, updated.size, removed.size, affectedShortcutsCount)
    }

    private suspend fun applyPiggybankChanges(comparisonResult: ComparisonResult<IEntity>): DbChangeResult {
        @Suppress("UNCHECKED_CAST")
        val added = comparisonResult.added as List<PiggybankEntity>
        added.forEach { localFireflyRepository.insertPiggybank(it) }

        @Suppress("UNCHECKED_CAST")
        val updated = comparisonResult.updated as List<PiggybankEntity>
        updated.forEach { localFireflyRepository.updatePiggybank(it) }

        @Suppress("UNCHECKED_CAST")
        val removed = comparisonResult.removed as List<PiggybankEntity>
        var affectedShortcutsCount = 0

        for (piggybank in removed) {
            val shortcuts = localFireflyRepository.getShortcutsByPiggybankId(piggybank.id)
            affectedShortcutsCount += shortcuts.size
            // room should set the foreign keys to NULL for us.
            localFireflyRepository.deletePiggybank(piggybank.id)
        }

        return DbChangeResult(added.size, updated.size, removed.size, affectedShortcutsCount)
    }

    private suspend fun applyTagChanges(comparisonResult: ComparisonResult<IEntity>): DbChangeResult {
        @Suppress("UNCHECKED_CAST")
        val added = comparisonResult.added as List<TagEntity>
        added.forEach { localFireflyRepository.insertTag(it) }

        @Suppress("UNCHECKED_CAST")
        val updated = comparisonResult.updated as List<TagEntity>
        updated.forEach { localFireflyRepository.updateTag(it) }

        @Suppress("UNCHECKED_CAST")
        val removed = comparisonResult.removed as List<TagEntity>
        var affectedShortcutsCount = 0

        for (tag in removed) {
            val shortcuts = localFireflyRepository.getShortcutsByTagId(tag.id)
            affectedShortcutsCount += shortcuts.size


            for (shortcut in shortcuts) {
                val shortcutWithTags = localFireflyRepository.getShortcutWithTags(shortcut.id)
                if (shortcutWithTags != null) {
                    // get current tag ids, excluding the one being deleted
                    val remainingTagIds = shortcutWithTags.tags
                        .filter { it.id != tag.id }
                        .map { it.id }

                    localFireflyRepository.saveShortcutWithTags(shortcut, remainingTagIds)
                }
            }

            localFireflyRepository.deleteTag(tag.id)
        }

        return DbChangeResult(added.size, updated.size, removed.size, affectedShortcutsCount)

    }

    private suspend fun applyChanges(comparisonResult: Map<KClass<out IEntity>, ComparisonResult<IEntity>>): SyncResult {
        val addedCount = mutableMapOf<KClass<out IEntity>, Int>()
        val updatedCount = mutableMapOf<KClass<out IEntity>, Int>()
        val removedCount = mutableMapOf<KClass<out IEntity>, Int>()
        val unchangedCount = mutableMapOf<KClass<out IEntity>, Int>()
        var deletedShortcutsCount = 0

        comparisonResult.forEach { (entityType, comparisonResult) ->
            unchangedCount[entityType] = comparisonResult.unchanged.size

            when (entityType) {
                AccountEntity::class -> {
                    val result = applyAccountChanges(comparisonResult)
                    addedCount[entityType] = result.added
                    updatedCount[entityType] = result.updated
                    removedCount[entityType] = result.removed
                    deletedShortcutsCount += result.deletedShortcuts
                }

                CategoryEntity::class -> {
                    val result = applyCategoryChanges(comparisonResult)
                    addedCount[entityType] = result.added
                    updatedCount[entityType] = result.updated
                    removedCount[entityType] = result.removed
                    deletedShortcutsCount += result.deletedShortcuts
                }

                BudgetEntity::class -> {
                    val result = applyBudgetChanges(comparisonResult)
                    addedCount[entityType] = result.added
                    updatedCount[entityType] = result.updated
                    removedCount[entityType] = result.removed
                    deletedShortcutsCount += result.deletedShortcuts
                }

                BillEntity::class -> {
                    val result = applyBillChanges(comparisonResult)
                    addedCount[entityType] = result.added
                    updatedCount[entityType] = result.updated
                    removedCount[entityType] = result.removed
                }

                PiggybankEntity::class -> {
                    val result = applyPiggybankChanges(comparisonResult)
                    addedCount[entityType] = result.added
                    updatedCount[entityType] = result.updated
                    removedCount[entityType] = result.removed
                }

                TagEntity::class -> {
                    val result = applyTagChanges(comparisonResult)
                    addedCount[entityType] = result.added
                    updatedCount[entityType] = result.updated
                    removedCount[entityType] = result.removed
                }
            }
        }

        return SyncResult(
            addedCount,
            updatedCount,
            removedCount,
            unchangedCount,
            deletedShortcutsCount
        )
    }


    private suspend fun fetchRemoteData() = coroutineScope {
        val accountsJob = async { fetchRemoteAccounts() }
        val billsJob = async { fetchRemoteBills() }
        val budgetsJob = async { fetchRemoteBudgets() }
        val categoriesJob = async { fetchRemoteCategories() }
        val piggybanksJob = async { fetchRemotePiggybanks() }
        val tagsJob = async { fetchRemoteTags() }

        // Wait for all jobs to complete
        listOf(accountsJob, billsJob, budgetsJob, categoriesJob, piggybanksJob, tagsJob).awaitAll()
    }

    private fun verifyFetchedData(): Boolean =
        !(_remoteAccounts == null ||
                _remoteBills == null ||
                _remoteBudgets == null ||
                _remoteCategories == null ||
                _remotePiggybanks == null ||
                _remoteTags == null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun fetchLocalData() = coroutineScope {
        // don't saturate IO thread pool
        val databaseDispatcher = Dispatchers.IO.limitedParallelism(3)

        val categoriesJob = async(databaseDispatcher) { fetchLocalCategories() }
        val accountsJob = async(databaseDispatcher) { fetchLocalAccounts() }
        val billsJob = async(databaseDispatcher) { fetchLocalBills() }
        val budgetsJob = async(databaseDispatcher) { fetchLocalBudgets() }
        val piggybanksJob = async(databaseDispatcher) { fetchLocalPiggybanks() }
        val tagsJob = async(databaseDispatcher) { fetchLocalTags() }

        listOf(categoriesJob, accountsJob, billsJob, budgetsJob, piggybanksJob, tagsJob).awaitAll()

        Log.d(TAG, "All local data loaded")
    }

    private fun verifyLocalData(): Boolean =
        !(_localAccounts == null ||
                _localBills == null ||
                _localBudgets == null ||
                _localCategories == null ||
                _localPiggyBanks == null ||
                _localTags == null)


    private fun generateTypeMap() {
        if (!verifyLocalData() || !verifyFetchedData()) {
            throw IllegalStateException("Either local or fetched data is invalid!")
        }

        _entityMaps = hashMapOf(
            CategoryEntity::class to EntityLists(_localCategories!!, _remoteCategories!!),
            AccountEntity::class to EntityLists(_localAccounts!!, _remoteAccounts!!),
            BillEntity::class to EntityLists(_localBills!!, _remoteBills!!),
            BudgetEntity::class to EntityLists(_localBudgets!!, _remoteBudgets!!),
            PiggybankEntity::class to EntityLists(_localPiggyBanks!!, _remotePiggybanks!!),
            TagEntity::class to EntityLists(_localTags!!, _remoteTags!!)
        )
    }

    private fun compareData(): Map<KClass<out IEntity>, ComparisonResult<IEntity>> {
        if (verifyLocalData() && verifyFetchedData()) {
            if (_entityMaps.isEmpty()) {
                generateTypeMap()
            }

            return _entityMaps.map { entry ->
                entry.key to EntityComparer.compareEntities(
                    entry.value.remoteList,
                    entry.value.localList
                )
            }.toMap()
        } else {
            throw IllegalStateException("Either local or fetched data is invalid!")
        }
    }


    //region Local Data Fetching
    private suspend fun fetchLocalCategories() {
        try {
            _localCategories = localFireflyRepository.getAllCategories()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching local categories", e)
        }
    }

    private suspend fun fetchLocalAccounts() {
        try {
            _localAccounts = localFireflyRepository.getAllAccounts()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching local accounts", e)
        }
    }

    private suspend fun fetchLocalBills() {
        try {
            _localBills = localFireflyRepository.getAllBills()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching local bills", e)
        }
    }

    private suspend fun fetchLocalBudgets() {
        try {
            _localBudgets = localFireflyRepository.getAllBudgets()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching local budgets", e)
        }
    }

    private suspend fun fetchLocalPiggybanks() {
        try {
            _localPiggyBanks = localFireflyRepository.getAllPiggybanks()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching local piggybanks", e)
        }
    }

    private suspend fun fetchLocalTags() {
        try {
            _localTags = localFireflyRepository.getAllTags()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching local tags", e)
        }
    }

    //endregion

    //region Remote Data Fetching

    private suspend fun fetchRemoteAccounts() {
        try {
            remoteFireflyRepository.getAccounts().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _remoteAccounts = result.data
                    }

                    is ApiResult.Error -> {
                        Log.e(TAG, "Error fetching accounts: ${result.message}")
                        _remoteAccounts = null
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchAccounts", e)
        }
    }

    private suspend fun fetchRemoteBills() {
        try {
            remoteFireflyRepository.getBills().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _remoteBills = result.data
                    }

                    is ApiResult.Error -> {
                        _remoteBills = null
                        Log.e(TAG, "Error fetching bills: ${result.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchBills", e)
        }
    }

    private suspend fun fetchRemoteBudgets() {
        try {
            remoteFireflyRepository.getBudgets().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _remoteBudgets = result.data
                    }

                    is ApiResult.Error -> {
                        _remoteBudgets = null
                        Log.e(TAG, "Error fetching budgets: ${result.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchBudgets", e)
        }
    }

    private suspend fun fetchRemoteCategories() {
        try {
            remoteFireflyRepository.getCategories().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _remoteCategories = result.data
                    }

                    is ApiResult.Error -> {
                        _remoteCategories = null
                        Log.e(TAG, "Error fetching categories: ${result.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchCategories", e)
        }
    }

    private suspend fun fetchRemotePiggybanks() {
        try {
            remoteFireflyRepository.getPiggybanks().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _remotePiggybanks = result.data
                    }

                    is ApiResult.Error -> {
                        _remotePiggybanks = null
                        Log.e(TAG, "Error fetching piggybanks: ${result.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchPiggybanks", e)

        }
    }

    private suspend fun fetchRemoteTags() {
        try {
            remoteFireflyRepository.getTags().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _remoteTags = result.data
                    }

                    is ApiResult.Error -> {
                        _remoteTags = null
                        Log.e(TAG, "Error fetching tags: ${result.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchTags", e)
        }
    }

    //endregion

    companion object {
        private const val TAG = "SyncService"
    }
}