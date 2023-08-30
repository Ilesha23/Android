package com.iyakovlev.task2.presentation.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.iyakovlev.task2.domain.Contact

class ContactsDiffCallback() : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}