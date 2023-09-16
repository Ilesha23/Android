package com.iyakovlev.contacts.data.model

import java.util.UUID

data class Contact(
    val id: UUID = UUID.randomUUID(),
    val photo: String,
    val name: String,
    val career: String,
    val address: String
)