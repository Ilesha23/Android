package com.iyakovlev.contacts.presentation.fragments.add_contact.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.iyakovlev.contacts.domain.model.UserRemote

class UsersDiffCallback : DiffUtil.ItemCallback<UserRemote>() {

    override fun areItemsTheSame(oldItem: UserRemote, newItem: UserRemote): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserRemote, newItem: UserRemote): Boolean {
        return oldItem == newItem
    }
}