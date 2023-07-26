package com.iyakovlev.task2.ui.contacts

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.iyakovlev.task2.databinding.ActivityContactsBinding
import com.iyakovlev.task2.data.viewmodel.ContactsViewModel
import androidx.activity.viewModels
import com.iyakovlev.task2.BaseActivity

class ContactsActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    private val vm: ContactsViewModel by viewModels()
    private val contactAdapter = ContactsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("AAA", "activity created")

        setupRecyclerView()
        observeContacts()

    }

    private fun setupRecyclerView() {
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            adapter = contactAdapter
        }
    }

    private fun observeContacts() {
        vm.contacts.observe(this) { contacts ->
            contactAdapter.setContacts(contacts)
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
////        contactsService.removeListener(contactsListener)
//    }
//
//    private val contactsListener: ContactsListener = {
//        adapter.contacts = it
//    }

}