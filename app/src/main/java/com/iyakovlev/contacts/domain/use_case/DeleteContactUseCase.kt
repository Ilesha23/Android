package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.repository.contacts.ContactsRepository
import javax.inject.Inject

class DeleteContactUseCase @Inject constructor(private val contactsRepository: ContactsRepository) {

    suspend operator fun invoke(contactId: Long): Resource<List<UserRemote>> {
        return contactsRepository.deleteContact(contactId)
    }

}