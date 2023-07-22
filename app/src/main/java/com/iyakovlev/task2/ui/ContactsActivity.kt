package com.iyakovlev.task2.ui

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.iyakovlev.task2.ContactsAdapter
import com.iyakovlev.task2.databinding.ActivityContactsBinding
import com.iyakovlev.task2.model.ContactsService

class ContactsActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val layoutManager = LinearLayoutManager(this)
        binding.rvContacts.layoutManager = layoutManager

        val contactsService = ContactsService.getInstance()
        adapter = ContactsAdapter(contactsService.getContacts())
        binding.rvContacts.adapter = adapter



    }

}