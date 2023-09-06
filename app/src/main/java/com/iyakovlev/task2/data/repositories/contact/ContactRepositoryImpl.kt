package com.iyakovlev.task2.data.repositories.contact

import android.content.ContentResolver
import android.provider.ContactsContract
import com.github.javafaker.Faker
import com.iyakovlev.task2.common.constants.Constants
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(private val contentResolver: ContentResolver) :
    ContactRepository {

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
                val career = getJobByContactId(contentResolver, id)
                val address = getAddressByContactId(contentResolver, id)

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
        _contacts.value = (1..20).map {
            Contact(
                //id = it.toLong(),
                name = faker.name().name(),
                career = faker.company().name(),
                photo = Constants.IMAGES[it % Constants.IMAGES.size],
                address = faker.address().fullAddress()
            )
        }.sortedBy {
            it.name
        }
    }

    override fun removeContact(contact: Contact) {
        _contacts.value = _contacts.value.filter { it != contact }
    }

    override fun removeContact(position: Int) {
        lastRemovedContact = _contacts.value[position]
        _contacts.value = _contacts.value.filterIndexed { index, _ -> index != position }
    }

    override fun addContact(contact: Contact) {
        lastRemovedContact = contact
        _contacts.value = _contacts.value.toMutableList().apply {
            add(findInsertionIndex(contact.name), contact)
        }
    }

    override fun undoRemoveContact() {
        lastRemovedContact?.let {
            addContact(it)
        }
        lastRemovedContact = null
        log("${_contacts.value.size}")
    }

    private fun findInsertionIndex(name: String): Int {
        val index = contacts.value.indexOfFirst { it.name.lowercase() > name.lowercase() }
        return if (index != -1) index else contacts.value.size
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

}