package com.iyakovlev.contacts.presentation.fragments.add_contact.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.databinding.ItemUserBinding
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.presentation.fragments.add_contact.diffutil.UsersDiffCallback
import com.iyakovlev.contacts.presentation.fragments.add_contact.interfaces.UserItemClickListener
import com.iyakovlev.contacts.presentation.utils.extensions.loadImageWithGlide

class UsersAdapter(val listener: UserItemClickListener) :
    ListAdapter<UserRemote, UsersAdapter.UserViewHolder>(UsersDiffCallback()) {

    private var isMultiSelect = false

    private var selectedContacts = mutableListOf<Long>()

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelectedPositions(list: List<Long>) {
        selectedContacts = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
        holder.itemView.setOnClickListener {
            selectedContacts.add(contact.id)
            holder.bind(contact)
            listener.onItemClick(contact.id)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isMultiSelect) {
            R.layout.item_user_selected
        } else {
            R.layout.item_user
        }
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: UserRemote) {
            with(binding) {
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                ivAvatar.loadImageWithGlide(contact.image)
                if (selectedContacts.contains(contact.id)) {
                    chkSelect.visibility = View.VISIBLE
                    tvAdd.visibility = View.GONE
                    ivContactRemove.visibility = View.GONE
                } else {
                    chkSelect.visibility = View.GONE
                    tvAdd.visibility = View.VISIBLE
                    ivContactRemove.visibility = View.VISIBLE
                }
            }
        }
    }

}
