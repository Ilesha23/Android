package com.iyakovlev.contacts.di

import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.domain.datastore.DataStore
import com.iyakovlev.contacts.domain.repository.user.UserRepository
import com.iyakovlev.contacts.domain.repository.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(api: ApiService, dataStore: DataStore): UserRepository {
        return UserRepositoryImpl(api, dataStore)
    }

}