package com.iyakovlev.contacts.presentation.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.UserRemote
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
class SearchViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
    val state = _state.asStateFlow()

    private val _cachedList = MutableStateFlow<List<UserRemote>>(emptyList())
    val cachedList = _cachedList.asStateFlow()

    init {
        log("contacts view model created", Constants.ISDEBUG)
//        viewModelScope.launch(Dispatchers.IO) {
//            _state.emit(getContactsUseCase())
//        }
//        updateContacts()
    }

    fun updateContacts() {
        log("contacts fr: contacts list updated", Constants.ISDEBUG)
        viewModelScope.launch(Dispatchers.IO) {
            val response = getContactsUseCase()
            _state.emit(response)
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
        log("${_cachedList.value}", Constants.ISDEBUG)
    }

}