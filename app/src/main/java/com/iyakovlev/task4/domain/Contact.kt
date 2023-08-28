package com.iyakovlev.task4.domain

import java.util.UUID

data class Contact(
    val id: UUID = UUID.randomUUID(),
    val photo: String,
    val name: String,
    val career: String,
    val address: String
)