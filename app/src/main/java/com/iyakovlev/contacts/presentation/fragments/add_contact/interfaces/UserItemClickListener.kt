package com.iyakovlev.contacts.presentation.fragments.add_contact.interfaces

import android.widget.ImageView

interface UserItemClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemClick(position: Int)
    fun onItemDeleteClick(position: Int)
    fun onItemLongClick(position: Int)
}