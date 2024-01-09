package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.data.model.UserRemote
import com.iyakovlev.contacts.data.repository.contacts.ContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val contactsRepository: ContactsRepository) {

    suspend operator fun invoke(): Resource<List<UserRemote>> {
        return contactsRepository.fetch()
    }

}