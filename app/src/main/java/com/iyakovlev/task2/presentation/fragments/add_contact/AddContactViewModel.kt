package com.iyakovlev.task2.presentation.fragments.add_contact

import androidx.lifecycle.ViewModel
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.data.repositories.contact.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {

    private val _photo = MutableStateFlow("")
    val photo = _photo.asStateFlow()

    fun setPhoto(url: String) {
        _photo.value = url
    }

    fun addContact(contact: Contact) {
        repository.addContact(contact)
    }

}