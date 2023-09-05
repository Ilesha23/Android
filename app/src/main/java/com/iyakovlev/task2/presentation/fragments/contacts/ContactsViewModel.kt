package com.iyakovlev.task2.presentation.fragments.contacts

import androidx.lifecycle.ViewModel
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.data.repositories.contact.ContactRepository
import com.iyakovlev.task2.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val isDebug = false

    val contacts = repository.contacts

    init {
        log("view model created", isDebug)
    }

    fun createFakeContacts() {
        if (contacts.value.isEmpty()) {
            repository.createFakeContacts()
        }
        log("default contacts created", isDebug)
    }

    fun removeContact(contact: Contact) {
        repository.removeContact(contact)
    }

    fun removeContact(position: Int) {
        repository.removeContact(position)
    }

    fun undoRemoveContact() {
        repository.undoRemoveContact()
    }

    fun addContact(contact: Contact) {
        repository.addContact(contact)
    }

    fun getContact(index: Int): Contact {
        return contacts.value[index]
    }

    fun loadContactsFromStorage() {
        repository.loadContactsFromStorage()
    }

    override fun onCleared() {
        log("view model cleared", isDebug)
        super.onCleared()
    }

}