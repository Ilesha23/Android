package com.iyakovlev.task2.domain

import android.content.ContentResolver

interface ContactRepository {

    suspend fun loadContactsFromStorage(contentResolver: ContentResolver):  List<Contact>

}