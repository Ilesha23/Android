package com.iyakovlev.contacts.di

import android.app.Application
import com.iyakovlev.contacts.domain.repository.contacts.ContactRep
import com.iyakovlev.contacts.domain.repository.contacts.ContactRepImpl
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
    fun provideRepository(application: Application): ContactRep {
        return ContactRepImpl(application.contentResolver)
    }
}