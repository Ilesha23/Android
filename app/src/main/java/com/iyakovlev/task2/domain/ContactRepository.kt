package com.iyakovlev.task2.domain

import android.content.ContentResolver

interface ContactRepository {

    fun loadContactsFromStorage(contentResolver: ContentResolver): List<Contact>

    fun createFakeContacts(): List<Contact>

}