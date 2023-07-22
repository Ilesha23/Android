package com.iyakovlev.task2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyakovlev.task2.databinding.ItemUserBinding
import com.iyakovlev.task2.model.Contact

class ContactsAdapter(private val contacts: List<Contact>)
    : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return ContactsViewHolder(binding)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            tvContactName.text = contact.name
            tvContactCareer.text = contact.career
            if (contact.photo.isNotBlank()) {
                Glide.with(ivAvatar.context)
                    .load(contact.photo)
                    .circleCrop()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_person_24)
                    .into(ivAvatar)
            } else {
                Glide.with(ivAvatar.context).clear(ivAvatar)
                ivAvatar.setImageResource(R.drawable.baseline_person_24)
            }
        }
    }

    class ContactsViewHolder (
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)
}