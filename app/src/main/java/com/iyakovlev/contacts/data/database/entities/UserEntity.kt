package com.iyakovlev.contacts.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iyakovlev.contacts.data.model.UserRemote

// TODO:
@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "career") val career: String? = null,
    @ColumnInfo(name = "birthday") val birthday: String? = null,
    @ColumnInfo(name = "image") val image: String? = null,
) {
    fun toUserRemote() = UserRemote (
        id, name, phone, address, career, birthday, image
    )
}