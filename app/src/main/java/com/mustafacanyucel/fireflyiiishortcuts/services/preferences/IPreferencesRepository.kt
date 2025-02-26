package com.mustafacanyucel.fireflyiiishortcuts.services.preferences

import kotlinx.coroutines.flow.Flow

interface IPreferencesRepository {
    suspend fun saveString(key: String, value: String)
    suspend fun getString(key: String, defaultValue: String): String
    fun getStringFlow(key: String, defaultValue: String): Flow<String>

    val serverUrlKey: String
    val clientIdKey: String
    val registeredRedirectUrl: String
}