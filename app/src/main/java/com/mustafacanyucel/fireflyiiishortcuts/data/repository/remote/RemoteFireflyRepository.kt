package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionRequest
import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionResponse
import com.mustafacanyucel.fireflyiiishortcuts.services.firefly.FireflyIiiApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles all remote data operations from Firefly III API.
 * This class combines all individual remote repositories into a single entry point.
 */
@Singleton
class RemoteFireflyRepository @Inject constructor(
    private val remoteAccountRepository: IRemoteAccountRepository,
    private val remoteBillRepository: IRemoteBillRepository,
    private val remoteBudgetRepository: IRemoteBudgetRepository,
    private val remoteCategoryRepository: IRemoteCategoryRepository,
    private val remotePiggybankRepository: IRemotePiggybankRepository,
    private val remoteTagRepository: IRemoteTagRepository,
    private val remoteTransactionRepository: IRemoteTransactionRepository,
    private val apiService: FireflyIiiApiService
) {
    /**
     * Test the connection to the Firefly III server
     * @return ApiResult with true if connection is successful, error otherwise
     */
    suspend fun testConnection(): ApiResult<Boolean> {
        return try {
            val result = apiService.testConnection()
            Log.d(TAG, "Connection test successful")
            ApiResult.Success(result)
        } catch (e: Exception) {
            Log.e(TAG, "Connection test failed: ${e.message}", e)
            ApiResult.Error("Connection test failed: ${e.message}", exception = e)
        }
    }

    // Account methods
    suspend fun getAccounts(
        type: String? = null, date: String? = null
    ): Flow<ApiResult<List<AccountEntity>>> {
        Log.d(TAG, "Fetching accounts with type: $type, date: $date")
        return remoteAccountRepository.getAccounts(type, date)
    }

    // Bill methods
    suspend fun getBills(): Flow<ApiResult<List<BillEntity>>> {
        Log.d(TAG, "Fetching bills")
        return remoteBillRepository.getBills()
    }

    // Budget methods
    suspend fun getBudgets(): Flow<ApiResult<List<BudgetEntity>>> {
        Log.d(TAG, "Fetching budgets")
        return remoteBudgetRepository.getBudgets()
    }

    // Category methods
    suspend fun getCategories(): Flow<ApiResult<List<CategoryEntity>>> {
        Log.d(TAG, "Fetching categories")
        return remoteCategoryRepository.getCategories()
    }

    // Piggybank methods
    suspend fun getPiggybanks(): Flow<ApiResult<List<PiggybankEntity>>> {
        Log.d(TAG, "Fetching piggybanks")
        return remotePiggybankRepository.getPiggybanks()
    }

    // Tag methods
    suspend fun getTags(): Flow<ApiResult<List<TagEntity>>> {
        Log.d(TAG, "Fetching tags")
        return remoteTagRepository.getTags()
    }

    // Transaction (shortcut) methods
    suspend fun createTransaction(transactionRequest: TransactionRequest): Flow<ApiResult<TransactionResponse>> {
        Log.d(TAG, "Creating transaction: $transactionRequest")
        return remoteTransactionRepository.createTransaction(transactionRequest)
    }

    /**
     * Fetch all data from the Firefly III API at once.
     * This method is useful for a full sync operation.
     * The returned map contains the results for each data type.
     */
    suspend fun getAllData(): Map<String, Flow<ApiResult<*>>> {
        Log.d(TAG, "Starting synchronization of all data")

        return mapOf(
            "accounts" to getAccounts(),
            "bills" to getBills(),
            "budgets" to getBudgets(),
            "categories" to getCategories(),
            "piggybanks" to getPiggybanks(),
            "tags" to getTags()
        )
    }

    companion object {
        private const val TAG = "RemoteFireflyRepository"
    }
}