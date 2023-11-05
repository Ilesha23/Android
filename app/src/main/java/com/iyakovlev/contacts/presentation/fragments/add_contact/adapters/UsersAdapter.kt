package com.iyakovlev.contacts.presentation.fragments.add_contact.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.databinding.ItemUserBinding
import com.iyakovlev.contacts.databinding.ItemUserSelectedBinding
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.presentation.fragments.add_contact.diffutil.UsersDiffCallback
import com.iyakovlev.contacts.presentation.fragments.add_contact.interfaces.UserItemClickListener
import com.iyakovlev.contacts.presentation.utils.extensions.loadImageWithGlide

class UsersAdapter(val listener: UserItemClickListener) :
    ListAdapter<UserRemote, UsersAdapter.UserViewHolder>(UsersDiffCallback()) {

    private var isMultiSelect = false

    private var selectedContacts = mutableListOf<Long>()
//    private var contacts: List<UserRemote>? = listOf()

//    @SuppressLint("NotifyDataSetChanged")
//    fun changeSelectionState(isSelection: Boolean) {
//        isMultiSelect = isSelection
//        notifyDataSetChanged()
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelectedPositions(list: List<Long>) {
        selectedContacts = list.toMutableList()
        notifyDataSetChanged()
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun changeContacts(list: List<UserRemote>?) {
//        contacts = list
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val isContain = sele.any { user2 -> user1.id == user2.id }
//        if (!isMultiSelect) {
            val binding =
                ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return UserViewHolder(binding)
//        }
//        val binding =
//            ItemUserSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return UserSelectedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        when (holder) {
//            is UserViewHolder -> holder.bind(getItem(position))
//            is UserSelectedViewHolder -> holder.bind(getItem(position))
//        }
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
                ivAvatar.loadImageWithGlide(contact.image.toString())
//                ivContactRemove.setOnClickListener {
//                    listener.onItemDeleteClick(bindingAdapterPosition)
//                }

//                if (selectedContacts.isNotEmpty()) {
//                    if (contacts!!.contains(selectedContacts[bindingAdapterPosition + 1])) {
//                        chkSelect.visibility = View.VISIBLE
//                        tvAdd.visibility = View.GONE
//                        ivContactRemove.visibility = View.GONE
//                    } else {
//                        chkSelect.visibility = View.GONE
//                        tvAdd.visibility = View.VISIBLE
//                        ivContactRemove.visibility = View.VISIBLE
//                    }
//                }

//                if (isSelected and (selectedContacts.contains(contact.id)/*getItem(bindingAdapterPosition).id == contact.id*/)) {
                if (selectedContacts.contains(contact.id)) {
                    chkSelect.visibility = View.VISIBLE
                    tvAdd.visibility = View.GONE
                    ivContactRemove.visibility = View.GONE
                } else {
                    chkSelect.visibility = View.GONE
                    tvAdd.visibility = View.VISIBLE
                    ivContactRemove.visibility = View.VISIBLE
                }

                clContactItem.setOnClickListener {
//                    if (chkSelect.visibility == View.GONE) {
//                        listener.onItemClick(contact.id, ivAvatar)
//                        chkSelect.visibility = View.VISIBLE
//                        tvAdd.visibility = View.GONE
//                        ivContactRemove.visibility = View.GONE
//                    }

//                    listener.onItemClick(contact.id)
//                    if (selectedContacts.contains(contact.id) and isSelected) {
//                        chkSelect.visibility = View.VISIBLE
//                        tvAdd.visibility = View.GONE
//                        ivContactRemove.visibility = View.GONE
//                    }
                }
//                clContactItem.setOnLongClickListener {
//                    listener.onItemLongClick(bindingAdapterPosition)
//                    true
//                }
            }
        }
    }

    inner class UserSelectedViewHolder(private val binding: ItemUserSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: UserRemote) {
            with(binding) {
//                chkSelect.isChecked = selectedContacts.contains(bindingAdapterPosition)
//                chkSelect.isChecked = contacts!!.contains(selectedContacts[bindingAdapterPosition + 1]) // TODO: BAD!!!
//                if (contacts!!.contains(selectedContacts[bindingAdapterPosition + 1])) {
//                    chkSelect.visibility = View.VISIBLE
//
//                } else {
//
//                }
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                ivAvatar.loadImageWithGlide(contact.image.toString())
                clContactItem.setOnClickListener {
//                    listener.onItemClick(bindingAdapterPosition)
                    chkSelect.toggle()
                }
            }
        }

        fun tog(pos: Int) {

        }

    }

}
