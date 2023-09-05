package com.iyakovlev.task2.presentation.fragments.contacts

import android.content.ContentResolver
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.iyakovlev.task2.data.repositories.contact.ContactRepositoryImpl
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/*@HiltViewModel*/
class ContactsViewModel /*@Inject constructor()*/ : ViewModel()/*, Parcelable*/ {

    private val isDebug = false

    private val contactRepository = ContactRepositoryImpl()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private var lastRemovedContact: Contact? = null
    private var lastRemovedContactIndex: Int? = null

    init {
        log("view model created", isDebug)
    }

    fun createFakeContacts() {
        if (contacts.value.isEmpty()) {
            _contacts.value = contactRepository.createFakeContacts()
        }
        log("default contacts created", isDebug)
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
        log("$contact added", isDebug)
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
        log("view model cleared", isDebug)
        super.onCleared()
    }

//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(p0: Parcel, p1: Int) {
//
//    }
}