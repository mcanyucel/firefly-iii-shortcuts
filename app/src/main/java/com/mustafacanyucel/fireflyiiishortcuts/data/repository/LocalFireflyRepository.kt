package com.mustafacanyucel.fireflyiiishortcuts.data.repository

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.model.api.account.AccountData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles all local data storage operations
 */
@Singleton
class LocalFireflyRepository @Inject constructor(
    private val localAccountRepository: ILocalAccountRepository,
    private val localCategoryRepository: ILocalCategoryRepository,
    private val localBudgetRepository: ILocalBudgetRepository,
    private val localTagRepository: ILocalTagRepository,
    private val localPiggybankRepository: ILocalPiggybankRepository,
    private val localBillRepository: ILocalBillRepository,

    ) {

    suspend fun getAllAccounts(): Flow<List<AccountEntity>> {
        return localAccountRepository.getAllAccounts()
    }

    suspend fun insertAccount(account: AccountData) {
        localAccountRepository.insertAccount(account)
    }



    companion object {
        private const val TAG = "LocalFireflyRepository"
    }
}