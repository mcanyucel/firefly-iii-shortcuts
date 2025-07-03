package com.mustafacanyucel.fireflyiiishortcuts.data.repository.local

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import kotlinx.coroutines.flow.Flow

interface ILocalAutocutRepository {
    suspend fun saveAutocuts(autocuts: List<AutocutEntity>)
    suspend fun getAllAutocuts(): List<AutocutEntity>
    suspend fun getAutocutById(id: Long): AutocutEntity?
    suspend fun insertAutocut(autocut: AutocutEntity): Long
    suspend fun updateAutocut(autocut: AutocutEntity): Int
    suspend fun deleteAutocut(autocut: AutocutEntity): Int
    fun observeAllAutocuts(): Flow<List<AutocutEntity>>

    // AutocutFilter relationship methods
    suspend fun saveAutocutWithFilter(autocut: AutocutEntity, filterIds: List<String>): Long
    suspend fun deleteAutocutFiltersForAutocut(autocutId: Long)
    suspend fun getAutocutWithAutocutFilters(autocutId: Long): AutocutEntity?
    suspend fun getAllAutocutsWithAutocutFilters(): List<AutocutEntity>

}