package com.iyakovlev.contacts.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iyakovlev.contacts.data.database.Database
import com.iyakovlev.contacts.data.database.repository.DatabaseRepository
import com.iyakovlev.contacts.utils.AppStatus.isFirstLaunch
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
    fun provideDatabase(@ApplicationContext applicationContext: Context): Database {
        // todo: maybe delete this hack
        val dc = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                isFirstLaunch = true
            }
        }
        return Room.databaseBuilder(
            applicationContext,
            Database::class.java, "contacts"
        )
//            .fallbackToDestructiveMigration()
            .addCallback(dc)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(db: Database): DatabaseRepository {
        return DatabaseRepository(db)
    }

}