package com.iyakovlev.task2.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.utils.Constants.IMAGES

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts


    init {
        val faker = Faker.instance()
        _contacts.value = (1..20).map { Contact(
            id = it.toLong(),
            name = faker.name().name(),
            career = faker.company().name(),
            photo = IMAGES[it % IMAGES.size]
        ) }
        Log.e("AAA", "view model created")
    }

    override fun onCleared() {
        Log.e("AAA", "view model cleared")
        super.onCleared()
    }
}