package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the accounts table
 */
@Dao
interface AccountDao {
    /**
     * Insert a single account
     * Note: Since we're using a String ID as the primary key and it's pre-defined,
     * this will return the row ID (which should be the same as the PK in the case of Room)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(accountEntity: AccountEntity): Long

    /**
     * Insert multiple accounts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accountEntities: List<AccountEntity>)

    /**
     * Update an existing account
     * @return the number of accounts updated (should be 1)
     */
    @Update
    suspend fun updateAccount(accountEntity: AccountEntity): Int

    /**
     * Get a single account by ID
     */
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: String): AccountEntity?

    /**
     * Get all accounts
     */
    @Query("SELECT * FROM accounts ORDER BY name")
    suspend fun getAllAccounts(): List<AccountEntity>

    /**
     * Delete an account by ID
     * @return the number of accounts deleted (should be 1)
     */
    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccountById(id: String): Int

    /**
     * Delete all accounts
     */
    @Query("DELETE FROM accounts")
    suspend fun deleteAllAccounts()

    /**
     * Observe all accounts as a Flow
     */
    @Query("SELECT * FROM accounts ORDER BY name")
    fun observeAllAccounts(): Flow<List<AccountEntity>>

    /**
     * Get accounts by active status
     */
    @Query("SELECT * FROM accounts WHERE active = :isActive ORDER BY name")
    suspend fun getAccountsByActiveStatus(isActive: Boolean): List<AccountEntity>

    /**
     * Get accounts by type
     */
    @Query("SELECT * FROM accounts WHERE accountType = :type ORDER BY name")
    suspend fun getAccountsByType(type: String): List<AccountEntity>

    /**
     * Transaction to replace all accounts
     * This is useful for syncing data from the server
     */
    @Transaction
    suspend fun replaceAllAccounts(accounts: List<AccountEntity>) {
        deleteAllAccounts()
        insertAccounts(accounts)
    }
}