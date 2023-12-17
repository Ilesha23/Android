package com.iyakovlev.contacts.presentation.utils.extensions

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import java.lang.Float.max
import java.lang.Float.min

// TODO:
fun RecyclerView.setButtonScrollListener(
    checkPosition: (Boolean) -> Unit
) {
    this.viewTreeObserver.addOnScrollChangedListener {
        val layoutManager = this.layoutManager as LinearLayoutManager
        val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
        val isButtonVisible = firstItem > 0
        checkPosition(isButtonVisible)
    }
}

inline fun <reified T: RecyclerView.ViewHolder>RecyclerView.addSwipe(crossinline callback: (T) -> Unit) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, 0) {
        override fun getSwipeDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return if (viewHolder is T) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (viewHolder is T) {
                callback(viewHolder)
            }
        }

    }).attachToRecyclerView(this)
}

fun RecyclerView.addSwipeToDelete(
    onSwiped: (Int) -> Unit/*,
    isEnabled: () -> Boolean*/
): ItemTouchHelper {

    var itemTouchHelper: ItemTouchHelper? = null

    itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0,
        /*ItemTouchHelper.END or */ItemTouchHelper.START
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

//        override fun isItemViewSwipeEnabled(): Boolean {
//            return isEnabled.invoke()
//        }

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

            val itemWidth = viewHolder.itemView.width.toFloat()
            val maxDX = if (dX > 0) min(dX, itemWidth) else max(dX, -itemWidth)

            val cornerRadius = context.resources.getDimension(R.dimen.vh_corner_radius)

            val background: RectF = if (dX > 0) {
                RectF(
                    viewHolder.itemView.left.toFloat(),
                    viewHolder.itemView.top.toFloat(),
                    viewHolder.itemView.left.toFloat() + maxDX,
                    viewHolder.itemView.bottom.toFloat()
                )
            } else {
                RectF(
                    viewHolder.itemView.right.toFloat() + maxDX,
                    viewHolder.itemView.top.toFloat(),
                    viewHolder.itemView.right.toFloat(),
                    viewHolder.itemView.bottom.toFloat()
                )
            }
            c.drawRoundRect(background, cornerRadius, cornerRadius, paint)

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


    })

    itemTouchHelper.attachToRecyclerView(this)
    return itemTouchHelper
}