package com.iyakovlev.task4.domain

import android.content.ContentResolver

interface ContactRepository {

    fun loadContactsFromStorage(contentResolver: ContentResolver): List<Contact>

    fun createFakeContacts(): List<Contact>

}