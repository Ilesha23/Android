package com.iyakovlev.contacts.data

data class UsersResponse(
    val code: Int,
    val data: Users,
    val message: String,
    val status: String
)

data class Users(
    val users: List<UserDto>
)