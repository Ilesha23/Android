package com.iyakovlev.contacts.domain.api.model

data class UserEditRequest(
    val address: String? = null,
    val birthday: String? = null,
    val career: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val twitter: String? = null,
)

data class UserGetResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: Data
)