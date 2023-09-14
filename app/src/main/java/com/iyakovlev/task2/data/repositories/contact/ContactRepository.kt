package com.iyakovlev.task2.data.repositories.contact

import com.iyakovlev.task2.data.model.Contact
import kotlinx.coroutines.flow.StateFlow

interface ContactRepository {

    val contacts: StateFlow<List<Contact>>

    fun loadContactsFromStorage(): List<Contact>

    fun createFakeContacts()

    fun removeSubList(sublist: List<Contact>)
    fun removeContact(contact: Contact)
    fun removeContact(position: Int)

    fun addContact(contact: Contact)

    fun undoRemoveContact()

}