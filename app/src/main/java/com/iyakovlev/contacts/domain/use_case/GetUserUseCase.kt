package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.UserGetRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<User> {
        return userRepository.getUser()
    }
}