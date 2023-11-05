package com.iyakovlev.contacts.presentation.fragments.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.Contact
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.repository.contacts.ContactRep
import com.iyakovlev.contacts.domain.repository.contacts.ContactsRepository
import com.iyakovlev.contacts.domain.use_case.AddContactUseCase
import com.iyakovlev.contacts.domain.use_case.DeleteContactUseCase
import com.iyakovlev.contacts.domain.use_case.GetContactsUseCase
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
//    private val repository: ContactsRepository
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val addContactUseCase: AddContactUseCase
) : ViewModel() {

//    val contacts = repository.state
    private val _state = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
    val state = _state.asStateFlow()

    private val _isMultiSelect = MutableStateFlow(false)
    val isMultiSelect = _isMultiSelect.asStateFlow()

    private val _selectedPositions = MutableStateFlow<List<Int>>(emptyList())
    val selectedPositions = _selectedPositions.asStateFlow()

    private var removedSelectionList = listOf<Contact>()

    private var removedContact: UserRemote? = null

    private var isRemoving = false

    init {
        log("contacts view model created", ISDEBUG)
//        viewModelScope.launch(Dispatchers.IO) {
//            _state.emit(getContactsUseCase())
//        }
//        updateContacts()
    }

    fun updateContacts() {
        _state.value.data?.toMutableList().apply {
            this?.clear()
        }
        viewModelScope.launch(Dispatchers.IO) {
//            _state.emit(getContactsUseCase())
            val response = getContactsUseCase()
            if (response is Resource.Success) {
                _state.emit(response)
            }
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            removedContact = _state.value.data?.find { it.id == id }
//            _state.emit(deleteContactUseCase(id))
            val response = deleteContactUseCase(id)
            if (response is Resource.Success) {
                _state.emit(response)
            }
        }
    }

    fun undoRemoveContact() {
        if (!isRemoving and (removedContact != null)) {
            isRemoving = true
            viewModelScope.launch(Dispatchers.IO) {
                val response = addContactUseCase(removedContact!!.id)
                if (response is Resource.Success) {
                    _state.emit(response)
                    removedContact = null
                    isRemoving = false
                }
            }
        }
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
//        log("default contacts created", ISDEBUG)
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

//    companion object {
//        val contacts = List<User>()
//    }

}