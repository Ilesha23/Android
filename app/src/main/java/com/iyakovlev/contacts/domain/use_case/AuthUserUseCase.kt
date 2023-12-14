package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.model.LoginRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthUserUseCase @Inject constructor(private val userRepository: UserRepository) {

//    suspend operator fun invoke(body: LoginRequest): Resource<User> {
//        return userRepository.loginUser(body)
//    }

    operator fun invoke(body: LoginRequest): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        emit(userRepository.loginUser(body))
    }

}