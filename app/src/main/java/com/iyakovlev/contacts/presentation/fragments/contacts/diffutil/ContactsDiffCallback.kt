package com.iyakovlev.contacts.presentation.fragments.contacts.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.iyakovlev.contacts.domain.model.Contact
import com.iyakovlev.contacts.domain.model.User

class ContactsDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}