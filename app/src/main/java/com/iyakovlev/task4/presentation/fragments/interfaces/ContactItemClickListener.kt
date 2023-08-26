package com.iyakovlev.task4.presentation.fragments.interfaces

import android.widget.ImageView

interface ContactItemClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemDeleteClick(position: Int)
}