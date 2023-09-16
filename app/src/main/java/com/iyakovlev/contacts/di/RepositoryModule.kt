package com.iyakovlev.contacts.di

import android.app.Application
import com.iyakovlev.contacts.data.repositories.contact.ContactRepository
import com.iyakovlev.contacts.data.repositories.contact.ContactRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(application: Application): ContactRepository {
        return ContactRepositoryImpl(application.contentResolver)
    }
}