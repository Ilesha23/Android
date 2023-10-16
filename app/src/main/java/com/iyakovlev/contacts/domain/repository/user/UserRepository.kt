package com.iyakovlev.contacts.domain.repository.user

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.LoginRequest
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.data.RegisterResponse
import com.iyakovlev.contacts.data.UserEditRequest
import com.iyakovlev.contacts.data.UserGetRequest
import com.iyakovlev.contacts.domain.model.User

interface UserRepository {

    suspend fun createUser(body: RegisterRequest): Resource<User>

    suspend fun editUser(body: UserEditRequest): Resource<User>

    suspend fun getUser(): Resource<User>

    suspend fun loginUser(body: LoginRequest): Resource<User>

}