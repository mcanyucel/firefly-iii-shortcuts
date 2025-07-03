package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterTagCrossRef
import kotlinx.coroutines.flow.Flow

/**
 * Simplified DAO for autocut filters
 */
@Dao
interface AutocutFilterDao {
    // Basic CRUD operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocutFilter(autocutFilterEntity: AutocutFilterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocutFilters(autocutFilterEntities: List<AutocutFilterEntity>)

    @Update
    suspend fun updateAutocutFilter(autocutFilterEntity: AutocutFilterEntity): Int

    @Query("SELECT * FROM autocut_filters WHERE id = :id")
    suspend fun getAutocutFilterById(id: Long): AutocutFilterEntity?

    @Query("SELECT * FROM autocut_filters ORDER BY name")
    suspend fun getAllAutocutFilters(): List<AutocutFilterEntity>

    @Query("DELETE FROM autocut_filters WHERE id = :id")
    suspend fun deleteAutocutFilterById(id: Long): Int

    @Delete
    suspend fun deleteAutocutFilter(autocutFilterEntity: AutocutFilterEntity): Int

    @Query("DELETE FROM autocut_filters")
    suspend fun deleteAllAutocutFilters()

    @Query("SELECT * FROM autocut_filters ORDER BY name")
    fun observeAllAutocutFilters(): Flow<List<AutocutFilterEntity>>

    // Queries for SMS matching
    @Query("SELECT * FROM autocut_filters WHERE transactionType = :transactionType ORDER BY name")
    suspend fun getAutocutFiltersByTransactionType(transactionType: String): List<AutocutFilterEntity>

    @Query("SELECT * FROM autocut_filters WHERE fromAccountMatch LIKE '%' || :match || '%' ORDER BY name")
    suspend fun getAutocutFiltersByFromAccountMatch(match: String): List<AutocutFilterEntity>

    @Query("SELECT * FROM autocut_filters WHERE toAccountMatch LIKE '%' || :match || '%' ORDER BY name")
    suspend fun getAutocutFiltersByToAccountMatch(match: String): List<AutocutFilterEntity>

    // Get filters for a specific autocut
    @Query("""
        SELECT af.* FROM autocut_filters af
        INNER JOIN autocut_autocutfilter_cross_ref aacr ON af.id = aacr.autocutFilterId
        WHERE aacr.autocutId = :autocutId
        ORDER BY af.name
    """)
    suspend fun getFiltersForAutocut(autocutId: Long): List<AutocutFilterEntity>

    // Tag association management
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilterTagCrossRef(crossRef: AutocutFilterTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilterTagCrossRefs(crossRefs: List<AutocutFilterTagCrossRef>)

    @Delete
    suspend fun deleteFilterTagCrossRef(crossRef: AutocutFilterTagCrossRef)

    @Query("DELETE FROM autocutfilter_tag_cross_ref WHERE autocutFilterId = :filterId")
    suspend fun deleteFilterTagCrossRefsForFilter(filterId: Long)

    @Query("SELECT tagId FROM autocutfilter_tag_cross_ref WHERE autocutFilterId = :filterId")
    suspend fun getTagIdsForFilter(filterId: Long): List<String>

    // Transaction operations
    @Transaction
    suspend fun setTagsForFilter(filterId: Long, tagIds: List<String>) {
        deleteFilterTagCrossRefsForFilter(filterId)
        val crossRefs = tagIds.map { tagId ->
            AutocutFilterTagCrossRef(filterId, tagId)
        }
        insertFilterTagCrossRefs(crossRefs)
    }

    @Transaction
    suspend fun replaceAllAutocutFilters(filters: List<AutocutFilterEntity>) {
        deleteAllAutocutFilters()
        insertAutocutFilters(filters)
    }
}