package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AutocutFilterDao {
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

    @Query("DELETE FROM autocut_filters")
    suspend fun deleteAllAutocutFilters(): Int

    @Delete
    suspend fun deleteAutocutFilter(autocutFilterEntity: AutocutFilterEntity): Int

    @Query("SELECT * FROM autocut_filters ORDER BY name")
    fun observeAllAutocutFilters(): Flow<List<AutocutFilterEntity>>
}