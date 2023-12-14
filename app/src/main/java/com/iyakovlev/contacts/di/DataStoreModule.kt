package com.iyakovlev.contacts.di

import com.iyakovlev.contacts.domain.datastore.DataStore
import com.iyakovlev.contacts.domain.datastore.DataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindDataStore(dataStoreImpl: DataStoreImpl): DataStore

}