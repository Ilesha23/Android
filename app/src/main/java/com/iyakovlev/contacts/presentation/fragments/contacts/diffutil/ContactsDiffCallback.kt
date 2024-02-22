package com.iyakovlev.contacts.presentation.fragments.contacts.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.iyakovlev.contacts.data.model.UserRemote

class ContactsDiffCallback : DiffUtil.ItemCallback<UserRemote>() {

    override fun areItemsTheSame(oldItem: UserRemote, newItem: UserRemote): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserRemote, newItem: UserRemote): Boolean {
        return oldItem == newItem
    }
}