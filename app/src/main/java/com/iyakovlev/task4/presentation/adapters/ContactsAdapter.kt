package com.iyakovlev.task4.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyakovlev.task4.R
import com.iyakovlev.task4.databinding.ItemUserBinding
import com.iyakovlev.task4.databinding.ItemUserSelectedBinding
import com.iyakovlev.task4.domain.Contact
import com.iyakovlev.task4.domain.ContactDiffItemCallback
import com.iyakovlev.task4.presentation.fragments.interfaces.ContactItemClickListener
import com.iyakovlev.task4.utils.extensions.loadImageWithGlide


class ContactsAdapter(val listener: ContactItemClickListener) :
    ListAdapter<Contact, RecyclerView.ViewHolder>(ContactDiffItemCallback()) {

    var isSelectionMode = false
    var selectedIndexesList = mutableListOf<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (isSelectionMode) {
            val binding = ItemUserSelectedBinding.inflate(inflater, parent, false)
            SelectedContactViewHolder(binding)
        } else {
            val binding = ItemUserBinding.inflate(inflater, parent, false)
            ContactViewHolder(binding)
        }
    }

    fun getContact(pos: Int): Contact = getItem(pos)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is ContactViewHolder) {
            holder.bind(getItem(position))
        } else if (holder is SelectedContactViewHolder) {
            holder.bind(getItem(position))
        }
    }

    inner class ContactViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                if (contact.photo.isNotBlank()) {
                    ivAvatar.loadImageWithGlide(contact.photo)
                } else {
                    Glide.with(ivAvatar.context).clear(ivAvatar)
                    ivAvatar.setImageResource(R.drawable.baseline_person_24)
                }
                ivContactRemove.setOnClickListener {
                    listener.onItemDeleteClick(adapterPosition)
                }
                clContactItem.setOnClickListener {
                    listener.onItemClick(adapterPosition, ivAvatar)
                }
                clContactItem.setOnLongClickListener {
                    listener.onItemLongClick(adapterPosition)
                    true
                }
            }
        }
    }

    inner class SelectedContactViewHolder(private val binding: ItemUserSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                chkSelect.isChecked = selectedIndexesList.contains(adapterPosition)
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                if (contact.photo.isNotBlank()) {
                    ivAvatar.loadImageWithGlide(contact.photo)
                } else {
                    Glide.with(ivAvatar.context).clear(ivAvatar)
                    ivAvatar.setImageResource(R.drawable.baseline_person_24)
                }
                clContactItem.setOnClickListener {
                    listener.onItemClick(adapterPosition, ivAvatar)
                    if (chkSelect.isChecked) {
                        selectedIndexesList.remove(adapterPosition)
                    } else {
                        selectedIndexesList.add(adapterPosition)
                    }
                    chkSelect.toggle()
                }
            }
        }
    }

}