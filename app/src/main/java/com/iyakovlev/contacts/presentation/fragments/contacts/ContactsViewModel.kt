package com.iyakovlev.contacts.presentation.fragments.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.BuildConfig
import com.iyakovlev.contacts.data.model.UserRemote
import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.domain.use_case.AddContactUseCase
import com.iyakovlev.contacts.domain.use_case.DeleteContactUseCase
import com.iyakovlev.contacts.domain.use_case.GetContactsUseCase
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val addContactUseCase: AddContactUseCase
) : ViewModel() {

    //    val contacts = repository.state
    private val _state = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
    val state = _state.asStateFlow()

    private val _cachedList = MutableStateFlow<List<UserRemote>>(emptyList())
    val cachedList = _cachedList.asStateFlow()

    private val _isMultiSelect = MutableStateFlow(false)
    val isMultiSelect = _isMultiSelect.asStateFlow()

    private val _selectedPositions = MutableStateFlow<List<Int>>(emptyList())
    val selectedPositions = _selectedPositions.asStateFlow()

    private var removedSelectionList = mutableListOf<Long>()

    private var removedContact: UserRemote? = null

    private var isRemoving = false

    init {
        log("contacts view model created", BuildConfig.DEBUG)
//        viewModelScope.launch(Dispatchers.IO) {
//            _state.emit(getContactsUseCase())
//        }
//        updateContacts()
    }

    fun updateContacts() {
        log("contacts fr: contacts list updated", BuildConfig.DEBUG)
        viewModelScope.launch(Dispatchers.IO) {
            val response = getContactsUseCase()
            _state.emit(response)
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            removedContact = _state.value.data?.find { it.id == id }
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
                val id = removedContact!!.id
                if (_state.value.data?.any { it.id == id } == false) {
                    val response = addContactUseCase(removedContact!!.id)
                    if (response is Resource.Success) {
                        _state.emit(response)
                        removedContact = null
                        isRemoving = false
                    }
                }
            }
        }
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

    fun removeSelectedContacts() {
        val indexesList = _selectedPositions.value.toList()
        _state.value.data?.apply {
            viewModelScope.launch(Dispatchers.IO) {
                for (c in indexesList) {
                    val id = this@apply.elementAt(c).id
                    removedSelectionList.add(id)
                    deleteContactUseCase(id)
                }
                updateContacts()
            }
            _selectedPositions.value.toMutableList().apply {
                clear()
            }
        }
    }

    fun changeSelectionState(isSelection: Boolean) {
        _isMultiSelect.value = isSelection
        if (!isSelection) {
            _selectedPositions.value = emptyList()
        }
    }

    fun undoRemoveContactsList() {
        viewModelScope.launch(Dispatchers.IO) {
            for (c in removedSelectionList) {
                addContactUseCase(c)
            }
            updateContacts()
            removedSelectionList.clear()
        }
    }

    fun setFilter(filter: String?) {
        if (filter.isNullOrBlank()) {
            _cachedList.value = _state.value.data ?: emptyList()
        } else {
            _cachedList.update {
                _state.value.data!!.filter {
                    it.name?.contains(filter, ignoreCase = true) ?: false
                }
            }
        }
        log("${_cachedList.value}", BuildConfig.DEBUG)
    }

    override fun onCleared() {
        log("view model cleared", BuildConfig.DEBUG)
        super.onCleared()
    }

}