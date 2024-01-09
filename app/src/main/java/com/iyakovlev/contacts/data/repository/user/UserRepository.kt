package com.iyakovlev.contacts.data.repository.user

import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.domain.api.model.LoginRequest
import com.iyakovlev.contacts.domain.api.model.RegisterRequest
import com.iyakovlev.contacts.domain.api.model.UserEditRequest
import com.iyakovlev.contacts.data.model.User

interface UserRepository {

    suspend fun createUser(body: RegisterRequest): Resource<User>

    suspend fun editUser(body: UserEditRequest): Resource<User>

    suspend fun getUser(): Resource<User>

    suspend fun loginUser(body: LoginRequest): Resource<User>

    suspend fun saveUserLogin(email: String, pass: String)

}