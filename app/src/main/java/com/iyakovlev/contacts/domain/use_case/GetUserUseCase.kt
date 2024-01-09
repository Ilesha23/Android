package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.data.model.User
import com.iyakovlev.contacts.data.repository.user.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<User> {
        return userRepository.getUser()
    }
}