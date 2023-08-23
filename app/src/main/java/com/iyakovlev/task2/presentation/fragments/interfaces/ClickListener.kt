package com.iyakovlev.task2.presentation.fragments.interfaces

import android.widget.ImageView

interface ClickListener {
    fun onItemClick(position: Int, imageView: ImageView)
    fun onItemDelete(position: Int)
}