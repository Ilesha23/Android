package com.iyakovlev.contacts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iyakovlev.contacts.data.database.entities.ContactEntity
import com.iyakovlev.contacts.data.database.interfaces.ContactDao

@Database(
    entities = [ContactEntity::class],
    version = 1
)
abstract class ContactDatabase: RoomDatabase() {

    abstract fun getContactsDao(): ContactDao

}