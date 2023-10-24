package com.iyakovlev.contacts.domain.datastore

interface DataStore {

    suspend fun get(key: String): String?
    suspend fun put(key: String, value: String)
    suspend fun delete(key: String)

}