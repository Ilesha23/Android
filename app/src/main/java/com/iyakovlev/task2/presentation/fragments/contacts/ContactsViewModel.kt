package com.iyakovlev.task2.presentation.fragments.contacts

import androidx.lifecycle.ViewModel
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.data.repositories.contact.ContactRepository
import com.iyakovlev.task2.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val isDebug = true

    val contacts = repository.contacts

    private val _isMultiSelect = MutableStateFlow(false)
    val isMultiSelect = _isMultiSelect.asStateFlow()

    private val _selectedPositions = MutableStateFlow<List<Int>>(emptyList())
    val selectedPositions = _selectedPositions.asStateFlow()

    private val removedSelectionList = listOf<Contact>()

    init {
        log("view model created", isDebug)
    }

    fun toggleSelectedPosition(position: Int) {
        if (_selectedPositions.value.contains(position)) {
            removeSelectedPosition(position)
            if (_selectedPositions.value.isEmpty()) {
                _isMultiSelect.value = false
            }
        } else {
            if (_isMultiSelect.value) {
                addSelectedPosition(position)
            }
        }
    }

    private fun addSelectedPosition(position: Int) {
        _selectedPositions.value = _selectedPositions.value.toMutableList().apply {
            add(position)
        }
    }

    private fun removeSelectedPosition(position: Int) {
        _selectedPositions.value = _selectedPositions.value.toMutableList().apply {
            remove(position)
        }
    }

    fun changeSelectionState(isSelection: Boolean) {
        _isMultiSelect.value = isSelection
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