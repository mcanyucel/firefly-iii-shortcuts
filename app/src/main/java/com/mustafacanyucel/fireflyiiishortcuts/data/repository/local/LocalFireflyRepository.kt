package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutWithTags
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles all local data storage operations.
 * This class combines all individual local repositories into a single entry point.
 *
 * Most of the methods are not used but created for completeness & future use.
 */
@Singleton
@Suppress("Unused")
class LocalFireflyRepository @Inject constructor(
    private val localAccountRepository: ILocalAccountRepository,
    private val localBillRepository: ILocalBillRepository,
    private val localBudgetRepository: ILocalBudgetRepository,
    private val localCategoryRepository: ILocalCategoryRepository,
    private val localPiggybankRepository: ILocalPiggybankRepository,
    private val localTagRepository: ILocalTagRepository,
    private val localShortcutRepository: ILocalShortcutRepository,
    private val localAutocutRepository: ILocalAutocutRepository,
    private val localAutocutFilterRepository: ILocalAutocutFilterRepository
) {
    // Account methods
    suspend fun saveAccounts(accounts: List<AccountEntity>) =
        localAccountRepository.saveAccounts(accounts)

    suspend fun getAllAccounts(): List<AccountEntity> = localAccountRepository.getAllAccounts()

    suspend fun getAccountById(id: String): AccountEntity? =
        localAccountRepository.getAccountById(id)

    suspend fun insertAccount(account: AccountEntity): String =
        localAccountRepository.insertAccount(account)

    suspend fun updateAccount(account: AccountEntity): Int =
        localAccountRepository.updateAccount(account)

    suspend fun deleteAccount(id: String): Int = localAccountRepository.deleteAccount(id)

    fun observeAllAccounts(): Flow<List<AccountEntity>> =
        localAccountRepository.observeAllAccounts()

    // Bill methods
    suspend fun saveBills(bills: List<BillEntity>) = localBillRepository.saveBills(bills)

    suspend fun getAllBills(): List<BillEntity> = localBillRepository.getAllBills()

    suspend fun getBillById(id: String): BillEntity? = localBillRepository.getBillById(id)

    suspend fun insertBill(bill: BillEntity): Long = localBillRepository.insertBill(bill)

    suspend fun updateBill(bill: BillEntity): Int = localBillRepository.updateBill(bill)

    suspend fun deleteBill(id: String): Int = localBillRepository.deleteBill(id)

    fun observeAllBills(): Flow<List<BillEntity>> = localBillRepository.observeAllBills()

    // Budget methods
    suspend fun saveBudgets(budgets: List<BudgetEntity>) =
        localBudgetRepository.saveBudgets(budgets)

    suspend fun getAllBudgets(): List<BudgetEntity> = localBudgetRepository.getAllBudgets()

    suspend fun getBudgetById(id: String): BudgetEntity? = localBudgetRepository.getBudgetById(id)

    suspend fun insertBudget(budget: BudgetEntity): Long =
        localBudgetRepository.insertBudget(budget)

    suspend fun updateBudget(budget: BudgetEntity): Int = localBudgetRepository.updateBudget(budget)

    suspend fun deleteBudget(id: String): Int = localBudgetRepository.deleteBudget(id)

    fun observeAllBudgets(): Flow<List<BudgetEntity>> = localBudgetRepository.observeAllBudgets()

    // Category methods
    suspend fun saveCategories(categories: List<CategoryEntity>) =
        localCategoryRepository.saveCategories(categories)

    suspend fun getAllCategories(): List<CategoryEntity> =
        localCategoryRepository.getAllCategories()

    suspend fun getCategoryById(id: String): CategoryEntity? =
        localCategoryRepository.getCategoryById(id)

    suspend fun insertCategory(category: CategoryEntity): String =
        localCategoryRepository.insertCategory(category)

    suspend fun updateCategory(category: CategoryEntity): Int =
        localCategoryRepository.updateCategory(category)

    suspend fun deleteCategory(id: String): Int = localCategoryRepository.deleteCategory(id)

    fun observeAllCategories(): Flow<List<CategoryEntity>> =
        localCategoryRepository.observeAllCategories()

    // Piggybank methods
    suspend fun savePiggybanks(piggybanks: List<PiggybankEntity>) =
        localPiggybankRepository.savePiggybanks(piggybanks)

    suspend fun getAllPiggybanks(): List<PiggybankEntity> =
        localPiggybankRepository.getAllPiggybanks()

    suspend fun getPiggybankById(id: String): PiggybankEntity? =
        localPiggybankRepository.getPiggybankById(id)

    suspend fun insertPiggybank(piggybank: PiggybankEntity): String =
        localPiggybankRepository.insertPiggybank(piggybank)

    suspend fun updatePiggybank(piggybank: PiggybankEntity): Int =
        localPiggybankRepository.updatePiggybank(piggybank)

    suspend fun deletePiggybank(id: String): Int = localPiggybankRepository.deletePiggybank(id)

    fun observeAllPiggybanks(): Flow<List<PiggybankEntity>> =
        localPiggybankRepository.observeAllPiggybanks()

    // Tag methods
    suspend fun saveTags(tags: List<TagEntity>) = localTagRepository.saveTags(tags)

    suspend fun getAllTags(): List<TagEntity> = localTagRepository.getAllTags()

    suspend fun getTagById(id: String): TagEntity? = localTagRepository.getTagById(id)

    suspend fun insertTag(tag: TagEntity): String = localTagRepository.insertTag(tag)

    suspend fun updateTag(tag: TagEntity): Int = localTagRepository.updateTag(tag)

    suspend fun deleteTag(id: String): Int = localTagRepository.deleteTag(id)

    fun observeAllTags(): Flow<List<TagEntity>> = localTagRepository.observeAllTags()

    // Shortcut methods
    suspend fun saveShortcuts(shortcuts: List<ShortcutEntity>) =
        localShortcutRepository.saveShortcuts(shortcuts)

    suspend fun getAllShortcuts(): List<ShortcutEntity> = localShortcutRepository.getAllShortcuts()

    suspend fun getShortcutById(id: Long): ShortcutEntity? =
        localShortcutRepository.getShortcutById(id)

    suspend fun insertShortcut(shortcut: ShortcutEntity): Long =
        localShortcutRepository.insertShortcut(shortcut)

    suspend fun updateShortcut(shortcut: ShortcutEntity): Int =
        localShortcutRepository.updateShortcut(shortcut)

    suspend fun deleteShortcut(id: Long): Int = localShortcutRepository.deleteShortcut(id)

    fun observeAllShortcuts(): Flow<List<ShortcutEntity>> =
        localShortcutRepository.observeAllShortcuts()

    // Shortcut with Tags methods
    suspend fun saveShortcutWithTags(shortcut: ShortcutEntity, tagIds: List<String>): Long =
        localShortcutRepository.saveShortcutWithTags(shortcut, tagIds)

    suspend fun deleteTagsForShortcut(shortcutId: Long) =
        localShortcutRepository.deleteTagsForShortcut(shortcutId)

    suspend fun getShortcutWithTags(shortcutId: Long): ShortcutWithTags? =
        localShortcutRepository.getShortcutWithTags(shortcutId)

    suspend fun getAllShortcutsWithTags(): List<ShortcutWithTags> =
        localShortcutRepository.getAllShortcutsWithTags()

    fun observeAllShortcutsWithTags(): Flow<List<ShortcutWithTags>> =
        localShortcutRepository.observeAllShortcutsWithTags()

    // Shortcut relationship query methods
    suspend fun getShortcutsByFromAccountId(accountId: String): List<ShortcutEntity> =
        localShortcutRepository.getShortcutsByFromAccountId(accountId)

    suspend fun getShortcutsByToAccountId(accountId: String): List<ShortcutEntity> =
        localShortcutRepository.getShortcutsByToAccountId(accountId)

    suspend fun getShortcutsByCategoryId(categoryId: String): List<ShortcutEntity> =
        localShortcutRepository.getShortcutsByCategoryId(categoryId)

    suspend fun getShortcutsByBudgetId(budgetId: String): List<ShortcutEntity> =
        localShortcutRepository.getShortcutsByBudgetId(budgetId)

    suspend fun getShortcutsByTagId(tagId: String): List<ShortcutEntity> =
        localShortcutRepository.getShortcutsByTagId(tagId)

    suspend fun getShortcutsByPiggybankId(piggybankId: String): List<ShortcutEntity> =
        localShortcutRepository.getShortcutsByPiggybankId(piggybankId)

    suspend fun getShortcutsByBillId(billId: String): List<ShortcutEntity> =
        localShortcutRepository.getShortcutsByBillId(billId)

    suspend fun updateShortcutLastUsed(id: Long, timestamp: Long = System.currentTimeMillis()) =
        localShortcutRepository.updateShortcutLastUsed(id, timestamp)

    // Simplified Autocut methods - no complex relationships
    suspend fun getAllAutocuts(): List<AutocutEntity> = localAutocutRepository.getAllAutocuts()

    suspend fun getAutocutById(id: Long): AutocutEntity? = localAutocutRepository.getAutocutById(id)

    suspend fun insertAutocut(autocut: AutocutEntity): Long = localAutocutRepository.insertAutocut(autocut)

    suspend fun updateAutocut(autocut: AutocutEntity): Int = localAutocutRepository.updateAutocut(autocut)

    suspend fun deleteAutocut(autocut: AutocutEntity): Int = localAutocutRepository.deleteAutocut(autocut)

    fun observeAllAutocuts(): Flow<List<AutocutEntity>> = localAutocutRepository.observeAllAutocuts()

    suspend fun getFilterIdsForAutocut(autocutId: Long): List<Long> =
        localAutocutRepository.getFilterIdsForAutocut(autocutId)

    suspend fun setFiltersForAutocut(autocutId: Long, filterIds: List<Long>) =
        localAutocutRepository.setFiltersForAutocut(autocutId, filterIds)

    // Simplified AutocutFilter methods
    suspend fun getAllAutocutFilters(): List<AutocutFilterEntity> =
        localAutocutFilterRepository.getAllAutocutFilters()

    suspend fun getAutocutFilterById(id: Long): AutocutFilterEntity? =
        localAutocutFilterRepository.getAutocutFilterById(id)

    suspend fun insertAutocutFilter(filter: AutocutFilterEntity): Long =
        localAutocutFilterRepository.insertAutocutFilter(filter)

    suspend fun updateAutocutFilter(filter: AutocutFilterEntity): Int =
        localAutocutFilterRepository.updateAutocutFilter(filter)

    suspend fun deleteAutocutFilter(filter: AutocutFilterEntity): Int =
        localAutocutFilterRepository.deleteAutocutFilter(filter)

    fun observeAllAutocutFilters(): Flow<List<AutocutFilterEntity>> =
        localAutocutFilterRepository.observeAllAutocutFilters()

    suspend fun getFiltersForAutocut(autocutId: Long): List<AutocutFilterEntity> =
        localAutocutFilterRepository.getFiltersForAutocut(autocutId)

    suspend fun getAutocutFiltersByTransactionType(transactionType: String): List<AutocutFilterEntity> =
        localAutocutFilterRepository.getAutocutFiltersByTransactionType(transactionType)

    suspend fun getAutocutFiltersByFromAccountMatch(match: String): List<AutocutFilterEntity> =
        localAutocutFilterRepository.getAutocutFiltersByFromAccountMatch(match)

    suspend fun getAutocutFiltersByToAccountMatch(match: String): List<AutocutFilterEntity> =
        localAutocutFilterRepository.getAutocutFiltersByToAccountMatch(match)

    suspend fun getTagIdsForFilter(filterId: Long): List<String> =
        localAutocutFilterRepository.getTagIdsForFilter(filterId)

    suspend fun setTagsForFilter(filterId: Long, tagIds: List<String>) =
        localAutocutFilterRepository.setTagsForFilter(filterId, tagIds)

    // Replacement for the complex observeAllAutocutsWithAutocutFilters
    // Just return basic autocuts since complex relationships are removed
    fun observeAllAutocutsWithAutocutFilters(): Flow<List<AutocutEntity>> =
        localAutocutRepository.observeAllAutocuts()

    companion object {
        private const val TAG = "LocalFireflyRepository"
    }
}