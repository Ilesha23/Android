package com.iyakovlev.task2.presentation.fragments.contacts.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.databinding.ItemUserBinding
import com.iyakovlev.task2.databinding.ItemUserSelectedBinding
import com.iyakovlev.task2.presentation.fragments.contacts.diffutil.ContactsDiffCallback
import com.iyakovlev.task2.presentation.fragments.contacts.interfaces.ContactItemClickListener
import com.iyakovlev.task2.presentation.utils.extensions.loadImageWithGlide
import com.iyakovlev.task2.R

class ContactsAdapter(val listener: ContactItemClickListener) :
    ListAdapter<Contact, RecyclerView.ViewHolder>(ContactsDiffCallback()) {

    private var isMultiSelect = false

    private var selectedPositions = listOf<Int>()

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelectionState(isSelection: Boolean) {
        isMultiSelect = isSelection
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelectedPositions(list: List<Int>) {
        selectedPositions = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!isMultiSelect) {
            val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ContactViewHolder(binding)
        }
        val binding = ItemUserSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactSelectedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactViewHolder -> holder.bind(getItem(position))
            is ContactSelectedViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isMultiSelect) {
            R.layout.item_user_selected // Use a different layout for multi-selection mode
        } else {
            R.layout.item_user
        }
    }

    inner class ContactViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            with(binding) {
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                ivAvatar.loadImageWithGlide(contact.photo)
                ivContactRemove.setOnClickListener {
                    listener.onItemDeleteClick(bindingAdapterPosition)
                }
                clContactItem.setOnClickListener {
                    listener.onItemClick(bindingAdapterPosition, ivAvatar)
                }
                clContactItem.setOnLongClickListener {
                    listener.onItemLongClick(bindingAdapterPosition)
                    true
                }
            }
        }
    }

    inner class ContactSelectedViewHolder(private val binding: ItemUserSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            with(binding) {
                chkSelect.isChecked = selectedPositions.contains(bindingAdapterPosition)
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                ivAvatar.loadImageWithGlide(contact.photo)
                clContactItem.setOnClickListener {
                    listener.onItemClick(bindingAdapterPosition)
                    chkSelect.toggle()
                }
            }
        }
    }

}
