package com.iyakovlev.task2.domain

import java.util.UUID

data class Contact (
    val id: UUID = UUID.randomUUID(),
    val photo: String?,
    val name: String,
    val career: String
)