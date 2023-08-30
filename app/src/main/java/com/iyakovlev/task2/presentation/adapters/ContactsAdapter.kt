package com.iyakovlev.task2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.ItemUserBinding
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.presentation.adapters.diffutil.ContactsDiffCallback
import com.iyakovlev.task2.presentation.fragments.interfaces.ContactItemClickListener
import com.iyakovlev.task2.utils.loadImageWithGlide


class ContactsAdapter(val listener: ContactItemClickListener) :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(ContactsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    fun getContact(pos: Int): Contact = getItem(pos)

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                if (contact.photo.isNotBlank()) {
                    binding.ivAvatar.loadImageWithGlide(contact.photo)
                } else {
                    Glide.with(ivAvatar.context).clear(ivAvatar)
                    ivAvatar.setImageResource(R.drawable.baseline_person_24)
                }
                ivContactRemove.setOnClickListener {
                    listener.onItemDeleteClick(bindingAdapterPosition)
                }
                clContactItem.setOnClickListener {
                    listener.onItemClick(bindingAdapterPosition, binding.ivAvatar)
                }
            }
        }
    }

}