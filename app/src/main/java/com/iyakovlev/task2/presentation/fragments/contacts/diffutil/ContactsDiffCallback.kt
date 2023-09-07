package com.iyakovlev.task2.presentation.fragments.contacts.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.iyakovlev.task2.data.model.Contact

class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}