package com.iyakovlev.contacts.di

import android.content.Context
import androidx.room.Room
import com.iyakovlev.contacts.data.database.ContactDatabase
import com.iyakovlev.contacts.data.database.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): ContactDatabase {
        return Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java, "contacts"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(db: ContactDatabase): DatabaseRepository {
        return DatabaseRepository(db)
    }

}