package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.UserEditRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.repository.user.UserRepository
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(body: UserEditRequest): Resource<User> {
        return userRepository.editUser(body)
    }

}