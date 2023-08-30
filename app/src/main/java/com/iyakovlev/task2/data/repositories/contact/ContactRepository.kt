package com.iyakovlev.task2.data.repositories.contact

import android.content.ContentResolver
import com.iyakovlev.task2.data.model.Contact

interface ContactRepository {

    fun loadContactsFromStorage(contentResolver: ContentResolver): List<Contact>    //todo app module

    fun createFakeContacts(): List<Contact>

}