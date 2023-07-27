package com.iyakovlev.task2.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.ItemUserBinding
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.utils.loadImageWithCoil
import com.iyakovlev.task2.utils.loadImageWithGlide
import com.iyakovlev.task2.utils.loadImageWithPicasso

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private var contacts: List<Contact> = emptyList()

    private var onRemoveClickListener: ((Contact) -> Unit)? = null
    fun setOnRemoveClickListener(listener: (Contact) -> Unit) {
        onRemoveClickListener = listener
    }

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
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
                if (contact.photo.isNotBlank()) {
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


//    var contacts: List<Contact> = emptyList()
//        set(newVal) {
//            field = newVal
//            notifyDataSetChanged()
//        }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ItemUserBinding.inflate(inflater, parent, false)
//        return ContactsViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = contacts.size
//
//    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
//        val contact = contacts[position]
//        with(holder.binding) {
//            tvContactName.text = contact.name
//            tvContactCareer.text = contact.career
//            if (contact.photo.isNotBlank()) {
//                Glide.with(ivAvatar.context)
//                    .load(contact.photo)
//                    .circleCrop()
//                    .placeholder(R.drawable.baseline_person_24)
//                    .error(R.drawable.baseline_person_24)
//                    .into(ivAvatar)
//            } else {
//                Glide.with(ivAvatar.context).clear(ivAvatar)
//                ivAvatar.setImageResource(R.drawable.baseline_person_24)
//            }
//        }
//    }
//
//    class ContactsViewHolder (
//        val binding: ItemUserBinding
//    ) : RecyclerView.ViewHolder(binding.root)
}