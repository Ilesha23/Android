package com.iyakovlev.task2.presentation.utils

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
        val position = parent.getChildAdapterPosition(view)
        val count = state.itemCount

        outRect.apply { // all elements common spacing
            left = spacing * 2
            right = spacing * 2
            top = spacing
            bottom = spacing
        }

        if (position == 0) { // top element spacing
            outRect.apply {
                top = spacing * 2
            }
        } else if (position == count - 1) { // bottom element spacing
            outRect.apply {
                bottom = lastSpacing
            }
        }
    }
}