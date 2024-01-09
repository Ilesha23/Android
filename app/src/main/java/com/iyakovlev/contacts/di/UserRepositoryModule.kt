package com.iyakovlev.contacts.di

import com.iyakovlev.contacts.domain.api.ApiService
import com.iyakovlev.contacts.data.database.repository.DatabaseRepository
import com.iyakovlev.contacts.domain.datastore.DataStore
import com.iyakovlev.contacts.data.repository.user.UserRepository
import com.iyakovlev.contacts.data.repository.user.UserRepositoryImpl
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
    fun provideUserRepository(api: ApiService, dataStore: DataStore, db: DatabaseRepository): UserRepository {
        return UserRepositoryImpl(api, dataStore, db)
    }

}