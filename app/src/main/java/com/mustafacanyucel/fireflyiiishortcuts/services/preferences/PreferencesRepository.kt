package com.mustafacanyucel.fireflyiiishortcuts.services.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : IPreferencesRepository {

    private val _changedKeyFlow = MutableSharedFlow<String>()
    override val changedKeyFlow: SharedFlow<String> = _changedKeyFlow.asSharedFlow()

    override suspend fun saveString(key: String, value: String) {
        dataStore.edit { preferences -> preferences[stringPreferencesKey(key)] = value }
        _changedKeyFlow.emit(key)
    }

    override suspend fun getString(key: String, defaultValue: String): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }.first()
    }

    override fun getStringFlow(key: String, defaultValue: String): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }
    }

    override val serverUrlKey: String = "server_url"
    override val clientIdKey: String = "client_id"
    override val registeredRedirectUrl: String = "registered_redirect_url"
}