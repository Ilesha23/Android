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

            while (it.moveToNext()) {
                val id = it.getLong(idColIndex)
                val name = it.getString(nameColIndex)
                val photo = it.getString(photoColIndex) ?: ""
                val career = getCareerByContactId(contentResolver, id) ?: ""

                val contact = Contact(id, photo, name, career)
                contactsList.add(contact)
            }
        }

        _contacts.value = contactsList

    }

    private fun getCareerByContactId(contentResolver: ContentResolver, contactId: Long): String? {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Organization.TITLE
        )

        val selection =
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"
        val selectionArgs = arrayOf(
            contactId.toString(),
            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
        )

        contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val careerColumnIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)
                return cursor.getString(careerColumnIndex)
            }
        }

        return null
    }

    override fun onCleared() {
        Log.e("AAA", "view model cleared")
        super.onCleared()
    }
}