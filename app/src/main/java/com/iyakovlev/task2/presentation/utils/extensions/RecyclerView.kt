package com.iyakovlev.task2.presentation.utils.extensions

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setButtonScrollListener(
    checkPosition: (Boolean) -> Unit
) {
    this.viewTreeObserver.addOnScrollChangedListener {
        val layoutManager = this.layoutManager as LinearLayoutManager
        val firstItem = layoutManager.findFirstVisibleItemPosition()
        val isButtonVisible = firstItem > 0
        checkPosition(isButtonVisible)
    }
}

fun RecyclerView.addSwipeToDelete(
    onSwiped: (Int) -> Unit
) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.END or ItemTouchHelper.START
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        )
                : Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val index = viewHolder.bindingAdapterPosition
            onSwiped(index)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val paint = Paint()
            paint.color = Color.RED

            val background: RectF = if (dX > 0) {
                RectF(
                    viewHolder.itemView.left.toFloat(),
                    viewHolder.itemView.top.toFloat(),
                    viewHolder.itemView.left.toFloat() + dX,
                    viewHolder.itemView.bottom.toFloat()
                )
            } else {
                RectF(
                    viewHolder.itemView.right.toFloat() + dX,
                    viewHolder.itemView.top.toFloat(),
                    viewHolder.itemView.right.toFloat(),
                    viewHolder.itemView.bottom.toFloat()
                )
            }
            c.drawRect(background, paint)

            super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }).attachToRecyclerView(this)
}