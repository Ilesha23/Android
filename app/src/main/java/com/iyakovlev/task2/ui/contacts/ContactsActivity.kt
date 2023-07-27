package com.iyakovlev.task2.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.iyakovlev.task2.databinding.ActivityContactsBinding
import com.iyakovlev.task2.data.viewmodel.ContactsViewModel
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iyakovlev.task2.BaseActivity
import com.iyakovlev.task2.utils.Constants.READ_CONTACTS_PERMISSION_REQUEST

class ContactsActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    private val vm: ContactsViewModel by viewModels()
    private val contactAdapter = ContactsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("AAA", "activity created")

        requestContactsReadPermission()

        setupRecyclerView()
        observeContacts()
        setupListeners()

    }

    private fun requestContactsReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST
            )
            Log.e("AAA", "permission to read contacts asked")
        } else {
            vm.loadContactsFromStorage(contentResolver)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load contacts
                Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show() // TODO:
                Log.e("AAA", "permission to read contacts granted")

                vm.loadContactsFromStorage(contentResolver)

            } else {
                // Permission denied, handle it accordingly (e.g., show a message)
                Toast.makeText(this, "not granted", Toast.LENGTH_SHORT).show() // TODO:
                Log.e("AAA", "permission to read contacts not granted")

                vm.createDefaultContacts()
                setupRecyclerView()
                observeContacts()

            }
        }
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

    private fun setupListeners() {
        contactAdapter.setOnRemoveClickListener { contact ->
            vm.removeContact(contact)
            Toast.makeText(this@ContactsActivity, "Contact removed", Toast.LENGTH_SHORT).show()
        }
    }

}