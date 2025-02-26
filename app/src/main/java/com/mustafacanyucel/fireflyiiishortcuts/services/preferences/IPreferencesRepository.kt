package com.mustafacanyucel.fireflyiiishortcuts.services.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface IPreferencesRepository {
    suspend fun saveString(key: String, value: String)
    suspend fun getString(key: String, defaultValue: String): String
    fun getStringFlow(key: String, defaultValue: String): Flow<String>
    val changedKeyFlow: SharedFlow<String>

    val serverUrlKey: String
    val clientIdKey: String
    val registeredRedirectUrl: String
}