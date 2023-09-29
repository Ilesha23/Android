package com.iyakovlev.contacts.domain.model

data class User(
    val id: Long = 0,
    val name: String?,
    val phone: String?,
    val address: String?,
    val career: String?,
    val birthday: String?,
    val image: String?,
    val accessToken: String? = "",
    val refreshToken: String? = ""
)
