package com.iyakovlev.contacts.domain.api.model

data class UsersResponse(
    val code: Int,
    val data: Users,
    val message: String,
    val status: String
)

data class Users(
    val users: List<UserDto>
)