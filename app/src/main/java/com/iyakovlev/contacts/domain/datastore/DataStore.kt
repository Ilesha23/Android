package com.iyakovlev.contacts.domain.datastore

import kotlinx.coroutines.flow.Flow

interface DataStore {

    suspend fun get(key: String): Flow<String>
    suspend fun put(key: String, value: String)
    suspend fun delete(key: String)

}