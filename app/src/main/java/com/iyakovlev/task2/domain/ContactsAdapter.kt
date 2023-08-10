package com.iyakovlev.task2.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.ItemUserBinding
import com.iyakovlev.task2.utils.loadImageWithGlide

class ContactsDiffCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private var contacts: List<Contact> = emptyList()

    private var onRemoveClickListener: ((Contact) -> Unit)? = null
    fun setOnRemoveClickListener(listener: (Contact) -> Unit) {
        onRemoveClickListener = listener
    }

    fun setContacts(contacts: List<Contact>) {
        val diffCallback = ContactsDiffCallback(this.contacts, contacts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.contacts = contacts
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position]) { clickedContact ->
            onRemoveClickListener?.invoke(clickedContact)
        }
    }

    class ContactViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact, onRemoveClickListener: (Contact) -> Unit) {
            binding.apply {
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                if (!contact.photo.isNullOrBlank()) {
                    binding.ivAvatar.loadImageWithGlide(contact.photo)
                } else {
                    Glide.with(ivAvatar.context).clear(ivAvatar)
                    ivAvatar.setImageResource(R.drawable.baseline_person_24)
                }
                ivContactRemove.setOnClickListener {
                    onRemoveClickListener.invoke(contact)
                }
            }
        }
    }

}