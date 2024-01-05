package com.iyakovlev.contacts.data.model

import com.iyakovlev.contacts.data.database.entities.ContactEntity
import com.iyakovlev.contacts.data.database.entities.UserEntity
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.model.UserRemote

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val birthday: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val image: String? = null,
)

data class RegisterResponse(
    val code: Int,
    val data: Data,
    val message: String,
    val status: String
)

data class Data(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)

data class UserDto(
    val address: String,
    val birthday: String,
    val career: String,
    val created_at: String,
    val email: String,
    val facebook: String,
    val id: Long,
    val image: String,
    val instagram: String,
    val linkedin: String,
    val name: String,
    val phone: String,
    val twitter: String,
    val updated_at: String
) {
    fun toUser(accessToken: String, refreshToken: String) = User(
        id, name, phone, address, career, birthday, image, accessToken, refreshToken
    )

    fun toUserRemote() = UserRemote(
        id, name, phone, address, career, birthday, image
    )

    fun toContactEntity() = ContactEntity(
        id, name, phone, address, career, birthday, image
    )

    fun toUserEntity() = UserEntity(
        id, name, phone, address, career, birthday, image
    )
}