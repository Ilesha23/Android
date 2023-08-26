package com.iyakovlev.task4.presentation.fragments.interfaces

import android.widget.ImageView

interface ClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemDelete(position: Int)
}