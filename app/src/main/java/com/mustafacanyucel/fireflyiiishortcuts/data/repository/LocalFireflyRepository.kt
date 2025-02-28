package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalTagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles all local data storage operations.
 * This class combines all individual local repositories into a single entry point.
 */
@Singleton
class LocalFireflyRepository @Inject constructor(
    private val localAccountRepository: ILocalAccountRepository,
    private val localBillRepository: ILocalBillRepository,
    private val localBudgetRepository: ILocalBudgetRepository,
    private val localCategoryRepository: ILocalCategoryRepository,
    private val localPiggybankRepository: ILocalPiggybankRepository,
    private val localTagRepository: ILocalTagRepository
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

    suspend fun getBillById(id: Long): BillEntity? = localBillRepository.getBillById(id)

    suspend fun insertBill(bill: BillEntity): Long = localBillRepository.insertBill(bill)

    suspend fun updateBill(bill: BillEntity): Int = localBillRepository.updateBill(bill)

    suspend fun deleteBill(id: Long): Int = localBillRepository.deleteBill(id)

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

}