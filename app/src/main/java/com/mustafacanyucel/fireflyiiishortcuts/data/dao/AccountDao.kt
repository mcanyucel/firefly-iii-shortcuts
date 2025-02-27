package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    /**
     * Get all accounts as a Flow
     */
    @Query("SELECT * FROM accounts ORDER BY name ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    /**
     * Get all accounts by type
     */
    @Query("SELECT * FROM accounts WHERE accountType = :type ORDER BY name ASC")
    fun getAccountsByType(type: String): Flow<List<AccountEntity>>

    /**
     * Get a single account by ID
     */
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: String): AccountEntity?

    /**
     * Insert a single account
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    /**
     * Insert multiple accounts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountEntity>)

    /**
     * Delete all accounts
     */
    @Query("DELETE FROM accounts")
    suspend fun deleteAllAccounts()

    /**
     * Get the count of accounts
     */
    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun getAccountCount(): Int

    /**
     * Get the last update timestamp
     */
    @Query("SELECT MAX(lastUpdated) FROM accounts")
    suspend fun getLastUpdateTimestamp(): Long?

    /**
     * Replaces all accounts in a single transaction
     * This ensures the database is never empty during the operation
     */
    @Transaction
    suspend fun replaceAllAccounts(accounts: List<AccountEntity>) {
        deleteAllAccounts()
        insertAccounts(accounts)
    }
}