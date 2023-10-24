package com.iyakovlev.contacts.presentation.fragments.contacts

import androidx.lifecycle.ViewModel
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.domain.model.Contact
import com.iyakovlev.contacts.domain.repository.contacts.ContactRep
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactRep
) : ViewModel() {

    private val isDebug = true

    val contacts = repository.contacts

    private val _isMultiSelect = MutableStateFlow(false)
    val isMultiSelect = _isMultiSelect.asStateFlow()

    private val _selectedPositions = MutableStateFlow<List<Int>>(emptyList())
    val selectedPositions = _selectedPositions.asStateFlow()

    private var removedSelectionList = listOf<Contact>()

    init {
        log("view model created", isDebug)
    }

    fun toggleSelectedPosition(position: Int) {
        if (_selectedPositions.value.contains(position)) {
//            removeSelectedPosition(position) todo
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

    // TODO:
//    private fun removeSelectedPosition(position: Int) {
//        _selectedPositions.value = _selectedPositions.value.toMutableList().apply {
//            remove(position)
//        }
//    }

    // TODO:
//    fun removeSelectedContacts() {
//        val indexesList = _selectedPositions.value.toList()
//        val contactsToRemove = indexesList.mapNotNull {
//            contacts.value.getOrNull(it)
//        }
//        removedSelectionList = contactsToRemove
//        _selectedPositions.value = emptyList()
//        changeSelectionState(false)
//        repository.removeSubList(contactsToRemove)
//    }

    fun changeSelectionState(isSelection: Boolean) {
        _isMultiSelect.value = isSelection
    }

//    fun createFakeContacts() {
//        if (contacts.value.isEmpty()) {
//            repository.createFakeContacts()
//        }
//        log("default contacts created", isDebug)
//    }

    // TODO:
//    fun removeContact(contact: Contact) {
//        repository.removeContact(contact)
//    }
//
//    fun removeContact(position: Int) {
//        repository.removeContact(position)
//    }
//
//    fun undoRemoveContact() {
//        repository.undoRemoveContact()
//    }
//
//    fun undoRemoveContactsList() {
//        for (c in removedSelectionList) {
//            repository.addContact(c)
//        }
//    }
//
//    fun addContact(contact: Contact) {
//        repository.addContact(contact)
//    }
//
//    fun getContact(index: Int): Contact {
//        return contacts.value[index]
//    }

//    fun loadContactsFromStorage() {
//        repository.loadContactsFromStorage()
//    }

    override fun onCleared() {
        log("view model cleared", ISDEBUG)
        super.onCleared()
    }

}