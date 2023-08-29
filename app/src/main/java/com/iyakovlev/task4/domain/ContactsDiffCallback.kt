package com.iyakovlev.task4.domain

import androidx.recyclerview.widget.DiffUtil

class ContactsDiffCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {

//    private var oldList: List<Contact>? = null
//    private var newList: List<Contact>? = null

//    fun setLists(old: List<Contact>, new: List<Contact>) {
//        oldList = old
//        newList = new
//    }

    override fun getOldListSize(): Int = oldList!!.size

    override fun getNewListSize(): Int = newList!!.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList!![oldItemPosition].id == newList!![newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList!![oldItemPosition] == newList!![newItemPosition]
    }

}