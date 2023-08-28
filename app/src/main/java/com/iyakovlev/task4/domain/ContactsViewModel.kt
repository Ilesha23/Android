package com.iyakovlev.task4.domain

import android.content.ContentResolver
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.iyakovlev.task4.data.ContactRepositoryImpl
import com.iyakovlev.task4.utils.Constants.LOG_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel : ViewModel(), Parcelable {

    private val contactRepository = ContactRepositoryImpl()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private val _selectedContacts = MutableStateFlow<List<Contact>>(emptyList())
    val selectedContacts = _selectedContacts.asStateFlow()

    private var lastRemovedContact: Contact? = null
    private var lastRemovedContactIndex: Int? = null

    init {
        Log.e(LOG_TAG, "view model created")
    }

    fun createFakeContacts() {
        if (contacts.value.isEmpty()) {
            _contacts.value = contactRepository.createFakeContacts()
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

//    private fun addToSelectedContacts(contact: Contact) {
//        val updatedSelectionList = _selectedContacts.value.toMutableList()
//        updatedSelectionList.add(contact)
//        _selectedContacts.value = updatedSelectionList
//        Log.e(LOG_TAG, "added to selection: $contact")
//    }
//
//    fun addToSelectedContacts(position: Int) {
//        val contact = _contacts.value[position]
//        addToSelectedContacts(contact)
//    }

    fun isSelectionMode(): Boolean {
        return _selectedContacts.value.isNotEmpty()
    }

    fun toggleSelection(position: Int) {
        val contact = _contacts.value[position]
        val updatedSelectionList = _selectedContacts.value.toMutableList()
        if (_selectedContacts.value.contains(contact)) {
            updatedSelectionList.remove(contact)
            Log.e(LOG_TAG, "removed from selection: $contact")
        } else {
            updatedSelectionList.add(contact)
            Log.e(LOG_TAG, "added to selection: $contact")
        }
        _selectedContacts.value = updatedSelectionList
    }

    fun removeSelectedContacts() {
        val updatedSelectionList = _selectedContacts.value.toMutableList()
        val updatedContactsList = _contacts.value.toMutableList()
        for (c in updatedSelectionList) {
            updatedContactsList.remove(c)
        }
        _contacts.value = updatedContactsList
        _selectedContacts.value = updatedSelectionList
        Log.e(LOG_TAG, "removed from selection")
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
        return if (index != -1) index else _contacts.value.size
    }

    fun loadContactsFromStorage(contentResolver: ContentResolver) {
        if (_contacts.value.isEmpty()) {
            _contacts.value = contactRepository.loadContactsFromStorage(contentResolver)
        }
    }

    override fun onCleared() {
        Log.e(LOG_TAG, "view model cleared")
        super.onCleared()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {

    }
}