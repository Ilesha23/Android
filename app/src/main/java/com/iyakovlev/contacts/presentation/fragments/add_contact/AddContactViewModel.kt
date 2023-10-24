package com.iyakovlev.contacts.presentation.fragments.add_contact

import androidx.lifecycle.ViewModel
import com.iyakovlev.contacts.domain.model.Contact
import com.iyakovlev.contacts.domain.repository.contacts.ContactRep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(private val repository: ContactRep) : ViewModel() {

    private val _photo = MutableStateFlow("")
    val photo = _photo.asStateFlow()

    fun setPhoto(url: String) {
        _photo.value = url
    }

    fun addContact(contact: Contact) {
        repository.addContact(contact)
    }

}