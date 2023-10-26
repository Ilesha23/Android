package com.iyakovlev.contacts.domain.repository.contacts

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.model.UserRemote
import kotlinx.coroutines.flow.StateFlow

interface ContactsRepository {

    val state: StateFlow<Resource<List<UserRemote>>>
    suspend fun fetch(): Resource<List<UserRemote>>

}