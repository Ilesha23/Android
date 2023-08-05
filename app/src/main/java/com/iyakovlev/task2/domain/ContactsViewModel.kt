package com.iyakovlev.task2.domain

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.iyakovlev.task2.utils.Constants.IMAGES
import com.iyakovlev.task2.utils.Constants.LOG_TAG
import java.util.UUID

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    private var lastRemovedContact: Contact? = null
    private var lastRemovedContactIndex: Int? = null

    init {
        Log.e("AAA", "view model created")
    }

    fun createDefaultContacts() {
        val faker = Faker.instance()
        _contacts.value = (1..20).map { Contact(
            //id = it.toLong(),
            name = faker.name().name(),
            career = faker.company().name(),
            photo = IMAGES[it % IMAGES.size]
        ) }.sortedBy {
            it.name//.lowercase()
        }
        Log.e(LOG_TAG, "default contacts created")
    }

    fun removeContact(contact: Contact) {
        val currentContacts = _contacts.value ?: return
        val updatedList = currentContacts.toMutableList()
        lastRemovedContactIndex = updatedList.indexOf(contact)
        updatedList.remove(contact)
        _contacts.value = updatedList
        lastRemovedContact = contact
    }

    fun undoRemoveContact() {
        lastRemovedContact?.let {
            addContact(lastRemovedContactIndex!!, it)
        }
        lastRemovedContact = null
        lastRemovedContactIndex = null
    }

    private fun addContact(index: Int, contact: Contact) {
        val currentContacts = _contacts.value ?: return
        val updatedContacts = currentContacts.toMutableList()
        updatedContacts.add(index, contact)
        _contacts.value = updatedContacts
        Log.e(LOG_TAG, "$contact added")
    }

    fun addContact(contact: Contact) {
        val index = findInsertionIndex(contact.name)
        if (index != -1) {
            addContact(index, contact)
        }
    }

    fun getContact(index: Int): Contact? {
        return _contacts.value?.get(index)
    }

    private fun findInsertionIndex(name: String): Int {
        val list = _contacts.value?.toMutableList() ?: return -1
        var left = 0
        var right = list.size - 1

        while (left <= right) {
            val mid = left + (right - left) / 2
            val midName = list[mid].name.lowercase()

            when {
                midName == name -> return mid
                midName < name -> left = mid + 1
                else -> right = mid - 1
            }
        }

        return left
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

                val uniqueString = "$id:$photo:$name:$career"
                val uuid = UUID.nameUUIDFromBytes(uniqueString.toByteArray())
                val contact = Contact(uuid, photo, name, career)
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