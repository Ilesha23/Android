package com.iyakovlev.task2.presentation.fragments.contacts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyakovlev.task2.R
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.databinding.ItemUserBinding
import com.iyakovlev.task2.presentation.fragments.contacts.diffutil.ContactsDiffCallback
import com.iyakovlev.task2.presentation.fragments.contacts.interfaces.ContactItemClickListener
import com.iyakovlev.task2.presentation.utils.extensions.loadImageWithGlide


class ContactsAdapter(val listener: ContactItemClickListener) :
ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(ContactsDiffCallback())
    /*RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>()*/ {

//    private var contacts: List<Contact> = emptyList()
//
//    fun setContacts(contacts: List<Contact>) {
//        val diffCallback = ContactsDiffCallback(this.contacts, contacts)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        this.contacts = contacts
//        diffResult.dispatchUpdatesTo(this)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

//    override fun getItemCount(): Int {
//        return contacts.size
//    }

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
                binding.ivAvatar.loadImageWithGlide(contact.photo)
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