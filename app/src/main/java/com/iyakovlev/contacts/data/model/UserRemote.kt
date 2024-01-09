package com.iyakovlev.contacts.data.model

data class UserRemote(
    val id: Long = 0,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val birthday: String? = null,
    val image: String? = null,
)
