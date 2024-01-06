package com.iyakovlev.contacts.data.database.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.iyakovlev.contacts.data.database.entities.ProfileEntity

@Dao
interface ProfileDao {

    @Upsert
    suspend fun insertProfile(user: ProfileEntity)

    @Query("select * from user")
    suspend fun getProfile(): ProfileEntity

    @Query("delete from user")
//    @Query("drop table user")
    suspend fun deleteProfile()

}