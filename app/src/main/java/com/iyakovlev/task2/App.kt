package com.iyakovlev.task2

import android.app.Application
import android.content.ContentResolver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this
    }

    companion object {
        private lateinit var app: App
        val contentResolver: ContentResolver get() = app.contentResolver
    }

}