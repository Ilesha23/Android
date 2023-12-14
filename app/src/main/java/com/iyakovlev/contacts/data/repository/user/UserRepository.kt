package com.iyakovlev.contacts.data.repository.user

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.model.LoginRequest
import com.iyakovlev.contacts.data.model.RegisterRequest
import com.iyakovlev.contacts.data.model.UserEditRequest
import com.iyakovlev.contacts.domain.model.User

interface UserRepository {

    suspend fun createUser(body: RegisterRequest): Resource<User>

    suspend fun editUser(body: UserEditRequest): Resource<User>

    suspend fun getUser(): Resource<User>

    suspend fun loginUser(body: LoginRequest): Resource<User>

    suspend fun saveUserLogin(email: String, pass: String)

}