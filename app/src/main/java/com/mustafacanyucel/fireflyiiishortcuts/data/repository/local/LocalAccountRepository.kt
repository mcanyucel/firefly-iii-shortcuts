package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.AccountDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Implementation of ILocalAccountRepository that uses Room database for storage
 * Uses AccountEntity for all inputs and outputs for consistency
 */
@Singleton
class LocalAccountRepository @Inject constructor(
    private val accountDao: AccountDao
) : ILocalAccountRepository {

    /**
     * Save a list of accounts to the database.
     * This implementation uses a transaction to replace all accounts at once.
     */
    override suspend fun saveAccounts(accounts: List<AccountEntity>) {
        try {
            // Use a transaction to ensure data consistency
            accountDao.replaceAllAccounts(accounts)
            Log.d(TAG, "Saved ${accounts.size} accounts to database")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving accounts to database", e)
            throw e
        }
    }

    /**
     * Get all accounts from the database
     */
    override suspend fun getAllAccounts(): List<AccountEntity> {
        return try {
            val accounts = accountDao.getAllAccounts()
            Log.d(TAG, "Retrieved ${accounts.size} accounts from database")
            accounts
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving accounts from database", e)
            throw e
        }
    }

    /**
     * Get an account by its ID
     */
    override suspend fun getAccountById(id: String): AccountEntity? {
        return try {
            val account = accountDao.getAccountById(id)
            if (account != null) {
                Log.d(TAG, "Retrieved account with id $id")
            } else {
                Log.d(TAG, "No account found with id $id")
            }
            account
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving account with id $id", e)
            throw e
        }
    }

    /**
     * Insert a new account and return its ID
     */
    override suspend fun insertAccount(account: AccountEntity): String {
        return try {
            // Insert and get the row ID (which should match the account ID since it's the PK)
            accountDao.insertAccount(account)
            Log.d(TAG, "Inserted account with id ${account.id}")
            account.id
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting account with id ${account.id}", e)
            throw e
        }
    }

    /**
     * Update an existing account
     * @return Number of rows updated (should be 1 if successful)
     */
    override suspend fun updateAccount(account: AccountEntity): Int {
        return try {
            val updatedRows = accountDao.updateAccount(account)
            Log.d(TAG, "Updated $updatedRows account(s) with id ${account.id}")
            updatedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error updating account with id ${account.id}", e)
            throw e
        }
    }

    /**
     * Delete an account by its ID
     * @return Number of rows deleted (should be 1 if successful)
     */
    override suspend fun deleteAccount(id: String): Int {
        return try {
            val deletedRows = accountDao.deleteAccountById(id)
            Log.d(TAG, "Deleted $deletedRows account(s) with id $id")
            deletedRows
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting account with id $id", e)
            throw e
        }
    }

    /**
     * Observe all accounts and emit updates when the data changes
     */
    override fun observeAllAccounts(): Flow<List<AccountEntity>> {
        return try {
            accountDao.observeAllAccounts()
        } catch (e: Exception) {
            Log.e(TAG, "Error observing accounts", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "LocalAccountRepository"
    }
}