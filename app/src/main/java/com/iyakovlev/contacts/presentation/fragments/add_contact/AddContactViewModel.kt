package com.iyakovlev.contacts.presentation.fragments.add_contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.data.model.UserRemote
import com.iyakovlev.contacts.domain.use_case.AddContactUseCase
import com.iyakovlev.contacts.domain.use_case.GetContactsUseCase
import com.iyakovlev.contacts.domain.use_case.GetUsersUseCase
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _cachedList = MutableStateFlow<List<UserRemote>>(emptyList())
    val cachedList = _cachedList.asStateFlow()

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
        }
    }

    fun toggleSelectedContact(id: Long) {
        _selectedContacts.value.toMutableList().apply {
            add(id)
        }
    }

    fun setFilter(filter: String?) {
        if (filter.isNullOrBlank()) {
            _cachedList.value = emptyList()
        } else {
            _cachedList.update {
                _state.value.data!!.filter {
                    it.name?.contains(filter, ignoreCase = true) ?: false
                }
            }
        }
        log("${_cachedList.value}", Constants.ISDEBUG)
    }

    fun addContact(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _addedContacts.emit(addContactUseCase(id))
        }
    }

}