package com.iyakovlev.contacts.presentation.fragments.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.databinding.ItemContactBinding
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.presentation.fragments.contacts.diffutil.ContactsDiffCallback
import com.iyakovlev.contacts.presentation.utils.extensions.loadImageWithGlide

class SearchAdapter :
    ListAdapter<UserRemote, SearchAdapter.ContactViewHolder>(ContactsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding =
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int) = R.layout.item_contact

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: UserRemote) {
            with(binding) {
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                ivAvatar.loadImageWithGlide(contact.image)
            }
        }
    }

}
