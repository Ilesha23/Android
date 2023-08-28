package com.iyakovlev.task4.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyakovlev.task4.R
import com.iyakovlev.task4.databinding.ItemUserBinding
import com.iyakovlev.task4.databinding.ItemUserSelectedBinding
import com.iyakovlev.task4.domain.Contact
import com.iyakovlev.task4.domain.ContactsDiffCallback
import com.iyakovlev.task4.presentation.fragments.interfaces.ContactItemClickListener
import com.iyakovlev.task4.utils.extensions.loadImageWithGlide


class ContactsAdapter(val listener: ContactItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder/*ContactViewHolder*//*ContactsAdapter.ContactViewHolder*/>() {

    private var contacts: List<Contact> = emptyList()
    var isSelectionMode = false

//    fun isSelectionMode(): Boolean {
//        return isSelectionMode
//    }

//    fun toggleSelectionMode() {
//        isSelectionMode = !isSelectionMode
//        notifyDataSetChanged() // Refresh the adapter to reflect the new mode
//    }

    fun setContacts(contacts: List<Contact>) {
        val diffCallback = ContactsDiffCallback(this.contacts, contacts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.contacts = contacts
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (isSelectionMode) {
            val binding = ItemUserSelectedBinding.inflate(inflater, parent, false)
            SelectedContactViewHolder(binding)
        } else {
            val binding = ItemUserBinding.inflate(inflater, parent, false)
            ContactViewHolder(binding)
        }
//        if (selectionMode) {
//            val binding = ItemUserSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return ContactViewHolder(binding)
//        }
//        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun getContact(pos: Int): Contact = contacts[pos]

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.bind(contacts[position])
        if (holder is ContactViewHolder) {
            holder.bind(contacts[position])
        } else if (holder is SelectedContactViewHolder) {
            holder.bind(contacts[position])
        }
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
                    listener.onItemDeleteClick(adapterPosition)
                }
                clContactItem.setOnClickListener {
                    listener.onItemClick(adapterPosition, binding.ivAvatar)
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
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                if (contact.photo.isNotBlank()) {
                    binding.ivAvatar.loadImageWithGlide(contact.photo)
                } else {
                    Glide.with(ivAvatar.context).clear(ivAvatar)
                    ivAvatar.setImageResource(R.drawable.baseline_person_24)
                }
                clContactItem.setOnClickListener {
                    listener.onItemAddToSelection(adapterPosition)
                }
            }
        }
    }

}