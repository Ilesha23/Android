package com.iyakovlev.task2.domain

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.javafaker.Faker
import com.iyakovlev.task2.data.ContactPepositoryImpl
import com.iyakovlev.task2.utils.Constants.IMAGES
import com.iyakovlev.task2.utils.Constants.LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ContactsViewModel : ViewModel() {

    private val contactRepository = ContactPepositoryImpl()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private var lastRemovedContact: Contact? = null
    private var lastRemovedContactIndex: Int? = null

    init {
        Log.e("AAA", "view model created")
    }

    fun createDefaultContacts() {
        val faker = Faker.instance()
        _contacts.value = (1..20).map {
            Contact(
                //id = it.toLong(),
                name = faker.name().name(),
                career = faker.company().name(),
                photo = IMAGES[it % IMAGES.size]
            )
        }.sortedBy {
            it.name
        }
        Log.e(LOG_TAG, "default contacts created")
    }

    fun removeContact(contact: Contact) {
        val currentContacts = _contacts.value ?: return
        val updatedList = currentContacts.toMutableList()
        lastRemovedContactIndex = updatedList.indexOf(contact)
        updatedList.remove(contact)
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
        val currentContacts = _contacts.value ?: return
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
        val list = _contacts.value?.toMutableList() ?: return -1
        var left = 0
        var right = list.size - 1

        while (left <= right) {
            val mid = left + (right - left) / 2
            val midName = list[mid].name.lowercase()

            when {
                midName == name -> return mid
                midName < name -> left = mid + 1
                else -> right = mid - 1
            }
        }

        return left
    }


    fun loadContactsFromStorage(contentResolver: ContentResolver) {
        viewModelScope.launch (Dispatchers.IO) {
            _contacts.value = contactRepository.loadContactsFromStorage(contentResolver)
        }
    }

        override fun onCleared() {
            Log.e("AAA", "view model cleared")
            super.onCleared()
        }
    }