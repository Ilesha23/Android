package com.iyakovlev.contacts.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(
    private var spacing: Int,
    private var lastSpacing: Int
) : RecyclerView.ItemDecoration() {
    init {
        spacing /= 2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply { // all elements common spacing
            left = spacing * 2
            right = spacing * 2
            top = spacing
            bottom = spacing
        }
    }
}