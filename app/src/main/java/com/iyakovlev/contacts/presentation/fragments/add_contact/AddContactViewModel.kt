package com.iyakovlev.contacts.presentation.fragments.add_contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.use_case.AddContactUseCase
import com.iyakovlev.contacts.domain.use_case.GetContactsUseCase
import com.iyakovlev.contacts.domain.use_case.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addContactUseCase: AddContactUseCase,
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
    val state = _state.asStateFlow()

    private val _selectedContacts = MutableStateFlow<List<Long/*UserRemote*/>>(emptyList())
    val selectedContacts = _selectedContacts.asStateFlow()

    private val _addedContacts = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
    val addedContacts = _addedContacts.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val users = getUsersUseCase()
            val contacts = getContactsUseCase()
            val list = users.data?.filter {
                !contacts.data!!.contains(it)
            }
            if (list != null) {
                _state.emit(Resource.Success(list))
            }
//            _state.emit(getUsersUseCase())
        }
    }

    fun toggleSelectedContact(id: Long) {
//        val id = _state.value.data?.find {
//            it.id == position + 1.toLong()
//        }
//        viewModelScope.launch(Dispatchers.IO) {
//            if (_selectedContacts.value.contains(id)) {
//                _selectedContacts.value.toMutableList().apply {
//                    removeAt(position)
//                }
//            } else {
//                _selectedContacts.value.toMutableList().apply {
//                    id?.let { add(it) } // TODO:
//                }
//            }
//        }

//        if (_selectedContacts.value.find { it.id == id } != null) {
//
//        }

        _selectedContacts.value.toMutableList().apply {
            add(id) // TODO:
        }

    }

    fun addContact(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _addedContacts.emit(addContactUseCase(id))
        }
    }

}