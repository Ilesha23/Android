package com.iyakovlev.contacts.domain.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iyakovlev.contacts.BuildConfig
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class DataStoreImpl @Inject constructor(@ApplicationContext private val context: Context) :
    DataStore {

    override suspend fun get(key: String): Flow<String> {
//        val dataStoreKey = stringPreferencesKey(key)
//        return if (context.dataStore.data.first().contains(dataStoreKey)) {
//            context.dataStore.data.first()[dataStoreKey]
//        } else {
//            null
//        }
        return flow {
            val dataStoreKey = stringPreferencesKey(key)
            val data = context.dataStore.data.first()[dataStoreKey].orEmpty()
            emit(data)
        }
    }

    override suspend fun put(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    override suspend fun delete(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings.remove(dataStoreKey)
            log("del", BuildConfig.DEBUG)
        }
    }

    companion object {
        private val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
            name = Constants.PREFERENCES
        )
    }

}