package com.iyakovlev.task2.data

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.domain.ContactRepository
import java.util.UUID

class ContactPepositoryImpl : ContactRepository {
    override suspend fun loadContactsFromStorage(contentResolver: ContentResolver) : List<Contact> {
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

        return contactsList

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


    }
