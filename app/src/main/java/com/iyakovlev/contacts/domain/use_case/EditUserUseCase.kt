package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.domain.api.model.UserEditRequest
import com.iyakovlev.contacts.data.model.User
import com.iyakovlev.contacts.data.repository.user.UserRepository
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(body: UserEditRequest): Resource<User> {
        return userRepository.editUser(body)
    }

}