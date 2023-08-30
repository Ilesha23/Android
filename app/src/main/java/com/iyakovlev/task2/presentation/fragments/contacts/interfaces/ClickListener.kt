package com.iyakovlev.task2.presentation.fragments.contacts.interfaces

import android.widget.ImageView

interface ClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemDelete(position: Int)
}