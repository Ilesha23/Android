package com.iyakovlev.task2.presentation.fragments.contacts.interfaces

import android.widget.ImageView

interface ContactItemClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemDeleteClick(position: Int)
}