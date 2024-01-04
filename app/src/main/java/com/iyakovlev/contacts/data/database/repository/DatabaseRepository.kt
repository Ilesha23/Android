package com.iyakovlev.contacts.data.database.repository

import com.iyakovlev.contacts.data.database.ContactDatabase
import com.iyakovlev.contacts.data.database.entities.ContactEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val contactDatabase: ContactDatabase
) {

    private val dao = contactDatabase.getContactsDao()

    suspend fun insertContactList(list: List<ContactEntity>) {
        withContext(Dispatchers.IO) {
            dao.insertContacts(list)
        }
    }

    suspend fun getContactList(): List<ContactEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext dao.getContacts()
        }
    }

}