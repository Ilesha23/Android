package com.iyakovlev.contacts.domain.repository.contacts

import com.iyakovlev.contacts.domain.model.Contact
import kotlinx.coroutines.flow.StateFlow

interface ContactRep {

    val contacts: StateFlow<List<Contact>>

    fun loadContactsFromStorage(): List<Contact>

    fun createFakeContacts()

    fun removeSubList(sublist: List<Contact>)
    fun removeContact(contact: Contact)
    fun removeContact(position: Int)

    fun addContact(contact: Contact)

    fun undoRemoveContact()

}