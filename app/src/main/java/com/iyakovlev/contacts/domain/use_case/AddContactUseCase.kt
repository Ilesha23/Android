package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.UserContactsResponse
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.repository.contacts.ContactsRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class AddContactUseCase @Inject constructor(/*@Named("Users") */private val contactsRepository: ContactsRepository) {

    suspend operator fun invoke(contactId: Long): Resource<List<UserRemote>> {
        return contactsRepository.addContact(contactId)
    }

}