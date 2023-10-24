package com.iyakovlev.contacts.domain.repository.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import com.github.javafaker.Faker
import com.iyakovlev.contacts.domain.model.Contact
import com.iyakovlev.contacts.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

class ContactRepImpl @Inject constructor(private val contentResolver: ContentResolver) :
    ContactRep {

    private val isDebug = true

    private val _contacts = MutableStateFlow(listOf<Contact>())
    override val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    private var lastRemovedContact: Contact? = null

    override fun loadContactsFromStorage(): List<Contact> {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
        )

        log("before cursor", isDebug)
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )
        log("after cursor", isDebug)

        val contactsList = mutableListOf<Contact>()
        cursor?.use {
            val idColIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameColIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val photoColIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

            while (it.moveToNext()) {
                val id = it.getLong(idColIndex)
                val name = it.getString(nameColIndex)
                val photo = it.getString(photoColIndex) ?: ""
                val career = getAdditionalInfoByContactId(
                    contentResolver,
                    id,
                    ContactsContract.CommonDataKinds.Organization.TITLE,
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                )
                val address = getAdditionalInfoByContactId(
                    contentResolver,
                    id,
                    ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
                )


                val uuid = UUID.randomUUID()
                val contact = Contact(uuid, photo, name, career, address)
                contactsList.add(contact)
            }
        }

        _contacts.tryEmit(contactsList)
        return contactsList
    }

    override fun createFakeContacts() {
        val faker = Faker.instance()
        _contacts.value = (1..CONTACTS_NUMBER).map {
            Contact(
                //id = it.toLong(),
                name = faker.name().name(),
                career = faker.company().name(),
                photo = IMAGES[it % IMAGES.size],
                address = faker.address().fullAddress()
            )
        }.sortedBy {
            it.name
        }
    }

    override fun removeSubList(sublist: List<Contact>) {
        _contacts.value = _contacts.value.toMutableList().apply {
            removeAll(sublist)
        }
    }

    override fun removeContact(contact: Contact) {
        lastRemovedContact = contact
        _contacts.value = _contacts.value.toMutableList().apply {
            remove(contact)
        }
    }

    override fun removeContact(position: Int) {
        _contacts.value = _contacts.value.toMutableList().apply {
            lastRemovedContact = this[position]
            removeAt(position)
        }
    }

    override fun addContact(contact: Contact) {
        _contacts.value = _contacts.value.toMutableList().apply {
            add(findInsertionIndex(contact.name), contact)
        }
    }

    override fun undoRemoveContact() {
        lastRemovedContact?.let {
            addContact(it)
        }
        lastRemovedContact = null
        log("${_contacts.value.size}", isDebug)
    }

    private fun findInsertionIndex(name: String): Int {
        val index = contacts.value.indexOfFirst { it.name.lowercase() > name.lowercase() }
        return if (index != -1) index else contacts.value.size
    }

    @SuppressLint("Range")
    private fun getAdditionalInfoByContactId(
        contentResolver: ContentResolver,
        contactId: Long,
        column: String,
        contentType: String
    ): String {
        val projection = arrayOf(column)

        val selection =
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = '$contentType'"
        val selectionArgs = arrayOf(contactId.toString())

        contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(column))
            }
        }

        return ""
    }

    companion object {
        const val CONTACTS_NUMBER = 20

        val IMAGES = listOf(
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80",
            "https://images.unsplash.com/photo-1527980965255-d3b416303d12?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1361&q=80",
            "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
            "https://images.unsplash.com/photo-1645830166230-187caf791b90?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80"
        )
    }

}