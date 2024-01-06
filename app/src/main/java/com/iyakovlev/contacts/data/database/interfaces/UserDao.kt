package com.iyakovlev.contacts.data.database.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iyakovlev.contacts.data.database.entities.ContactEntity
import com.iyakovlev.contacts.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("select * from users")
    suspend fun getUsers(): List<UserEntity>

    @Query("select * from users where id = :id")
    suspend fun getUser(id: Long): ContactEntity

    @Query("delete from users")
//    @Query("drop table users")
    suspend fun deleteUsers()

}