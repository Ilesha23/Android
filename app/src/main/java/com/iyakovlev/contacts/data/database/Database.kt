package com.iyakovlev.contacts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iyakovlev.contacts.data.database.entities.ContactEntity
import com.iyakovlev.contacts.data.database.entities.UserEntity
import com.iyakovlev.contacts.data.database.interfaces.ContactDao
import com.iyakovlev.contacts.data.database.interfaces.UserDao

@Database(
    entities = [ContactEntity::class, UserEntity::class],
    version = 2
)
abstract class Database: RoomDatabase() {

    abstract fun getContactsDao(): ContactDao
    abstract fun getUsersDao(): UserDao

}