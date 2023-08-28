package com.iyakovlev.task4.presentation.fragments.interfaces

import android.widget.ImageView

interface ContactItemClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemLongClick(position: Int)
    fun onItemAddToSelection(position: Int)
    fun onItemDeleteClick(position: Int)
}