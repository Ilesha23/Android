package com.iyakovlev.contacts.data

import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.model.UserRemote

//data class UserContactsRequest (
//
//)

data class UserContactsResponse (
    val code: Int,
    val data: Contacts,
    val message: String,
    val status: String
)

data class Contacts(
    val contacts: List<UserDto>
)