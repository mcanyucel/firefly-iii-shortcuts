package com.mustafacanyucel.fireflyiiishortcuts.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AutocutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocut(autocutEntity: AutocutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutocuts(autocutEntities: List<AutocutEntity>)

    @Update
    suspend fun updateAutocut(autocutEntity: AutocutEntity): Int

    @Query("SELECT * FROM autocuts where id = :id")
    suspend fun getAutocutById(id: Long): AutocutEntity?

    @Query("SELECT * FROM autocuts ORDER BY name")
    suspend fun getAllAutocuts(): List<AutocutEntity>

    @Query("DELETE FROM autocuts WHERE id = :id")
    suspend fun deleteAutocutById(id: Long): Int

    @Delete
    suspend fun deleteAutocut(autocutEntity: AutocutEntity): Int

    @Query("SELECT * FROM autocuts ORDER BY name")
    fun observeAllAutocuts(): Flow<List<AutocutEntity>>
}