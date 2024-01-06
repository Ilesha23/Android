package com.iyakovlev.contacts.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class ProfileEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "career") val career: String?,
    @ColumnInfo(name = "birthday") val birthday: String?,
    @ColumnInfo(name = "image") val image: String?,
)