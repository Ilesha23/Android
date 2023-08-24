package com.iyakovlev.task2.domain

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.iyakovlev.task2.data.ContactRepositoryImpl
import com.iyakovlev.task2.utils.Constants.IMAGES
import com.iyakovlev.task2.utils.Constants.LOG_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ContactsViewModel : ViewModel() {

    private val contactRepository = ContactRepositoryImpl()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private var lastRemovedContact: Contact? = null
    private var lastRemovedContactIndex: Int? = null

    init {
        Log.e(LOG_TAG, "view model created")
    }

    fun createFakeContacts() {
        _contacts.value = contactRepository.createFakeContacts()
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
        val index = _contacts.value.indexOfFirst { it.name.lowercase() > name.lowercase() }
        return if (index != -1) index else 0
    }

    fun loadContactsFromStorage(contentResolver: ContentResolver) {
        _contacts.value = contactRepository.loadContactsFromStorage(contentResolver)
    }

    override fun onCleared() {
        Log.e(LOG_TAG, "view model cleared")
        super.onCleared()
    }
}