package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.repository.contacts.ContactsRepository
import com.iyakovlev.contacts.domain.repository.users.UsersRepositoryImpl
import javax.inject.Inject
import javax.inject.Named

class GetUsersUseCase @Inject constructor(@Named("Users") private val usersRepository: ContactsRepository) {

    suspend operator fun invoke(): Resource<List<UserRemote>> {
        return usersRepository.fetch()
    }

}