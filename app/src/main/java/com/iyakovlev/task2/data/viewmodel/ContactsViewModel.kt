package com.iyakovlev.task2.data.viewmodel

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.utils.Constants.IMAGES

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts


    init {
//        val faker = Faker.instance()
//        _contacts.value = (1..20).map { Contact(
//            id = it.toLong(),
//            name = faker.name().name(),
//            career = faker.company().name(),
//            photo = IMAGES[it % IMAGES.size]
//        ) }
        Log.e("AAA", "view model created")
    }

    fun createDefaultContacts() {
        val faker = Faker.instance()
        _contacts.value = (1..20).map { Contact(
            id = it.toLong(),
            name = faker.name().name(),
            career = faker.company().name(),
            photo = IMAGES[it % IMAGES.size]
        ) }
    }

    fun loadContactsFromStorage(contentResolver: ContentResolver) {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
//            ContactsContract.CommonDataKinds.Organization.COMPANY
        )

        Log.e("AAA", "before cursor")
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )
        Log.e("AAA", "after cursor")

        val contactsList = mutableListOf<Contact>()
        cursor?.use {
            val idColIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameColIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val photoColIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
//            val companyColIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)

            while (it.moveToNext()) {
                val id = it.getLong(idColIndex)
                val name = it.getString(nameColIndex)
                val photo = it.getString(photoColIndex) ?: ""
//                val company = it.getString(companyColIndex) ?: ""

                val contact = Contact(id, photo, name, "company")
                contactsList.add(contact)
            }
        }

        _contacts.value = contactsList

    }

    override fun onCleared() {
        Log.e("AAA", "view model cleared")
        super.onCleared()
    }
}