package com.iyakovlev.contacts.di

import com.iyakovlev.contacts.data.api.ApiService
import com.iyakovlev.contacts.data.database.repository.DatabaseRepository
import com.iyakovlev.contacts.data.repository.contacts.ContactsRepository
import com.iyakovlev.contacts.data.repository.contacts.ContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContactsRepositoryModule {

    @Provides
    @Singleton
    fun provideContactsRepository(apiService: ApiService, db: DatabaseRepository): ContactsRepository {
        return ContactsRepositoryImpl(apiService, db)
    }

}