package com.iyakovlev.task4.data

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import com.github.javafaker.Faker
import com.iyakovlev.task4.domain.Contact
import com.iyakovlev.task4.domain.ContactRepository
import com.iyakovlev.task4.utils.Constants
import java.util.UUID

class ContactRepositoryImpl : ContactRepository {

    override fun loadContactsFromStorage(contentResolver: ContentResolver): List<Contact> {
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
                val career = getJobByContactId(contentResolver, id)
                val address = getAddressByContactId(contentResolver, id)

                val uuid = UUID.randomUUID()
                val contact = Contact(uuid, photo, name, career, address)
                contactsList.add(contact)
            }
        }
        return contactsList
    }

    override fun createFakeContacts(): List<Contact> {
        val faker = Faker.instance()
        return (1..20).map { Contact(
            //id = it.toLong(),
            name = faker.name().name(),
            career = faker.company().name(),
            photo = Constants.IMAGES[it % Constants.IMAGES.size],
            address = faker.address().fullAddress()
        ) }.sortedBy {
            it.name
        }
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