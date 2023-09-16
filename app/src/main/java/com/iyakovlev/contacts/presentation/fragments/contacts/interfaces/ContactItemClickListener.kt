package com.iyakovlev.contacts.presentation.fragments.contacts.interfaces

import android.widget.ImageView

interface ContactItemClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemClick(position: Int)
    fun onItemDeleteClick(position: Int)
    fun onItemLongClick(position: Int)
}