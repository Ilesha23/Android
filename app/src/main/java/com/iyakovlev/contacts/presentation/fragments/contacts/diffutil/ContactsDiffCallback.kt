package com.iyakovlev.contacts.presentation.fragments.contacts.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.iyakovlev.contacts.domain.model.UserRemote

class ContactsDiffCallback : DiffUtil.ItemCallback<UserRemote>() {

    override fun areItemsTheSame(oldItem: UserRemote, newItem: UserRemote): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserRemote, newItem: UserRemote): Boolean {
        return oldItem == newItem
    }
}