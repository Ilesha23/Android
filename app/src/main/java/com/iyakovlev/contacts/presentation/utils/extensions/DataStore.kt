package com.iyakovlev.contacts.presentation.utils.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iyakovlev.contacts.common.constants.Constants.PREFERENCES
import kotlinx.coroutines.flow.first

object DataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES)

    suspend fun get(context: Context, key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return if (preferences.contains(dataStoreKey)) {
            preferences[dataStoreKey]
        } else {
            null
        }
    }

    suspend fun put(context: Context, key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    suspend fun delete(context: Context, key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings.remove(dataStoreKey)
        }
    }

}