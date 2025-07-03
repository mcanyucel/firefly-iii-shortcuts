package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutAutocutFilterCrossRef
import kotlinx.coroutines.flow.Flow

/**
 * Simplified DAO - no complex relationships, just basic operations
 */
@Dao
interface AutocutDao {
    // Basic CRUD operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocut(autocutEntity: AutocutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocuts(autocutEntities: List<AutocutEntity>)

    @Update
    suspend fun updateAutocut(autocutEntity: AutocutEntity): Int

    @Query("SELECT * FROM autocuts WHERE id = :id")
    suspend fun getAutocutById(id: Long): AutocutEntity?

    @Query("SELECT * FROM autocuts ORDER BY name")
    suspend fun getAllAutocuts(): List<AutocutEntity>

    @Query("DELETE FROM autocuts WHERE id = :id")
    suspend fun deleteAutocutById(id: Long): Int

    @Delete
    suspend fun deleteAutocut(autocutEntity: AutocutEntity): Int

    @Query("DELETE FROM autocuts")
    suspend fun deleteAllAutocuts()

    @Query("SELECT * FROM autocuts ORDER BY name")
    fun observeAllAutocuts(): Flow<List<AutocutEntity>>

    // Filter association management
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocutFilterCrossRef(crossRef: AutocutAutocutFilterCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocutFilterCrossRefs(crossRefs: List<AutocutAutocutFilterCrossRef>)

    @Delete
    suspend fun deleteAutocutFilterCrossRef(crossRef: AutocutAutocutFilterCrossRef)

    @Query("DELETE FROM autocut_autocutfilter_cross_ref WHERE autocutId = :autocutId")
    suspend fun deleteAutocutFilterCrossRefsForAutocut(autocutId: Long)

    @Query("SELECT autocutFilterId FROM autocut_autocutfilter_cross_ref WHERE autocutId = :autocutId")
    suspend fun getFilterIdsForAutocut(autocutId: Long): List<Long>

    // Transaction operations
    @Transaction
    suspend fun setFiltersForAutocut(autocutId: Long, filterIds: List<Long>) {
        deleteAutocutFilterCrossRefsForAutocut(autocutId)
        val crossRefs = filterIds.map { filterId ->
            AutocutAutocutFilterCrossRef(autocutId, filterId)
        }
        insertAutocutFilterCrossRefs(crossRefs)
    }

    @Transaction
    suspend fun replaceAllAutocuts(autocuts: List<AutocutEntity>) {
        deleteAllAutocuts()
        insertAutocuts(autocuts)
    }
}