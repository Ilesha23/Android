package com.iyakovlev.contacts.domain.api.model

data class LoginRequest(
    val email: String,
    val password: String
)