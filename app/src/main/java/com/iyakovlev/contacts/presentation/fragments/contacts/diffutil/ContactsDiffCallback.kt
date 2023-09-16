package com.iyakovlev.contacts.presentation.fragments.contacts.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.iyakovlev.contacts.data.model.Contact

class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}