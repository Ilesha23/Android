package com.iyakovlev.contacts.data

import com.iyakovlev.contacts.domain.model.User

data class RegisterRequest(
    val email: String,
    val password: String
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
}

//fun Data.toUser(): User {
//    return User(
//        user.id,
//        user.name,
//        user.phone,
//        user.address,
//        user.career,
//        user.birthday,
//        user.image,
//        accessToken,
//        refreshToken
//    )
//}