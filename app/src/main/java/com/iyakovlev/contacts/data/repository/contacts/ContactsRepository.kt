package com.iyakovlev.contacts.data.repository.contacts

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.database.interfaces.ContactDao
import com.iyakovlev.contacts.domain.model.UserRemote
import kotlinx.coroutines.flow.StateFlow

interface ContactsRepository {

    val state: StateFlow<Resource<List<UserRemote>>>
    suspend fun getUsers(): Resource<List<UserRemote>>
    suspend fun fetch(): Resource<List<UserRemote>>
    suspend fun addContact(contactId: Long): Resource<List<UserRemote>>
    suspend fun deleteContact(contactId: Long): Resource<List<UserRemote>>

}