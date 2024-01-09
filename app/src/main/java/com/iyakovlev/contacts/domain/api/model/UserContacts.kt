package com.iyakovlev.contacts.domain.api.model


data class UserContactsResponse(
    val code: Int,
    val data: Contacts,
    val message: String,
    val status: String
)

data class Contacts(
    val contacts: List<UserDto>
)