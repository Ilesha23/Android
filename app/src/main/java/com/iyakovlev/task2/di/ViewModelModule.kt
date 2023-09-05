package com.iyakovlev.task2.di

import com.iyakovlev.task2.presentation.fragments.contacts.ContactsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun provideViewModel(): ContactsViewModel {
        return ContactsViewModel()
    }
}