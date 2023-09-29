package com.iyakovlev.contacts.domain.repository.user

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.data.RegisterResponse
import com.iyakovlev.contacts.domain.model.User

interface UserRepository {

    suspend fun createUser(body: RegisterRequest): RegisterResponse

}