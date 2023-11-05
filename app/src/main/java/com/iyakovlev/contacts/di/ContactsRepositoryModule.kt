package com.iyakovlev.contacts.di

import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.domain.repository.contacts.ContactsRepository
import com.iyakovlev.contacts.domain.repository.contacts.ContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContactsRepositoryModule {

    @Provides
    @Singleton
//    @Named("Contacts")
    fun provideContactsRepository(apiService: ApiService): ContactsRepository {
        return ContactsRepositoryImpl(apiService)
    }

//    @Provides
//    @Singleton
//    @Named("Users")
//    fun provideUsersRepository(apiService: ApiService): ContactsRepository {
//        return UsersRepositoryImpl(apiService)
//    }

}