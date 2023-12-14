package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.data.repository.contacts.ContactsRepository
import javax.inject.Inject
import javax.inject.Named

class GetContactsUseCase @Inject constructor(private val contactsRepository: ContactsRepository) {

    suspend operator fun invoke(): Resource<List<UserRemote>> {
        return contactsRepository.fetch()
    }

}