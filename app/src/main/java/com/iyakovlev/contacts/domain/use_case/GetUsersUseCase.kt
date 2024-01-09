package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.data.model.UserRemote
import com.iyakovlev.contacts.data.repository.contacts.ContactsRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(/*@Named("Users") */private val usersRepository: ContactsRepository) {

    suspend operator fun invoke(): Resource<List<UserRemote>> {
//        return usersRepository.fetch()
        return usersRepository.getUsers()
    }

}