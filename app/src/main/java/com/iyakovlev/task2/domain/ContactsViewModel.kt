package com.iyakovlev.task2.domain

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.iyakovlev.task2.utils.Constants.IMAGES
import com.iyakovlev.task2.utils.Constants.LOG_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> get() = _contacts

    private var lastRemovedContact: Contact? = null
    private var lastRemovedContactIndex: Int? = null

    init {
        Log.e(LOG_TAG, "view model created")
    }

    fun createDefaultContacts() {
        val faker = Faker.instance()
        _contacts.value = (1..20).map { Contact(
            //id = it.toLong(),
            name = faker.name().name(),
            career = faker.company().name(),
            photo = IMAGES[it % IMAGES.size],
            address = faker.address().fullAddress()
        ) }.sortedBy {
            it.name
        }
        Log.e(LOG_TAG, "default contacts created")
    }

    fun removeContact(contact: Contact) {
        val currentContacts = _contacts.value
        val updatedList = currentContacts.toMutableList()
        lastRemovedContactIndex = updatedList.indexOf(contact)
        updatedList.remove(contact)
        _contacts.value = updatedList
        lastRemovedContact = contact
    }

    fun removeContact(position: Int) {
        val currentContacts = _contacts.value
        val updatedList = currentContacts.toMutableList()
        lastRemovedContactIndex = position
        val contact = currentContacts[position]
        updatedList.removeAt(position)
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
        val currentContacts = _contacts.value
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
        return _contacts.value[index]
    }

    private fun findInsertionIndex(name: String): Int {
        val list = _contacts.value.toMutableList() ?: return -1
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

        Log.e(LOG_TAG, "before cursor")
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )
        Log.e(LOG_TAG, "after cursor")

        val contactsList = mutableListOf<Contact>()
        cursor?.use {
            val idColIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameColIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val photoColIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

            while (it.moveToNext()) {
                val id = it.getLong(idColIndex)
                val name = it.getString(nameColIndex)
                val photo = it.getString(photoColIndex) ?: ""
                val career = getJobByContactId(contentResolver, id)
                val address = getAddressByContactId(contentResolver, id)

                val contact = Contact(UUID.randomUUID(), photo, name, career, address)
                contactsList.add(contact)
            }
        }

        _contacts.value = contactsList

    }

    private fun getJobByContactId(contentResolver: ContentResolver, contactId: Long): String {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Organization.TITLE,
        )

        val selection =
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"
        val selectionArgs = arrayOf(
            contactId.toString(),
            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
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
                return cursor.getString(careerColumnIndex) ?: ""
            }
        }

        return ""
    }

    private fun getAddressByContactId(contentResolver: ContentResolver, contactId: Long): String {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
        )

        val selection =
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"
        val selectionArgs = arrayOf(
            contactId.toString(),
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
        )

        contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val addressColumnIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
                return cursor.getString(addressColumnIndex) ?: ""
            }
        }

        return ""
    }

    override fun onCleared() {
        Log.e(LOG_TAG, "view model cleared")
        super.onCleared()
    }
}