package com.iyakovlev.contacts.presentation.fragments.contacts.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.databinding.ItemContactBinding
import com.iyakovlev.contacts.databinding.ItemContactSelectedBinding
import com.iyakovlev.contacts.databinding.ItemUserBinding
import com.iyakovlev.contacts.databinding.ItemUserSelectedBinding
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.presentation.fragments.add_contact.diffutil.UsersDiffCallback
import com.iyakovlev.contacts.presentation.fragments.contacts.diffutil.ContactsDiffCallback
import com.iyakovlev.contacts.presentation.fragments.contacts.interfaces.ContactItemClickListener
import com.iyakovlev.contacts.presentation.utils.extensions.loadImageWithGlide

class ContactsAdapter(val listener: ContactItemClickListener) :
    ListAdapter<UserRemote, RecyclerView.ViewHolder>(ContactsDiffCallback()) {

    private var isMultiSelect = false

    private var selectedPositions = listOf<Int>()

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelectionState(isSelection: Boolean) {
        isMultiSelect = isSelection
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelectedPositions(list: List<Int>) {
        selectedPositions = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!isMultiSelect) {
            val binding =
                ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ContactViewHolder(binding)
        }
        val binding =
            ItemContactSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactSelectedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactViewHolder -> holder.bind(getItem(position))
            is ContactSelectedViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isMultiSelect) {
            R.layout.item_contact_selected
        } else {
            R.layout.item_contact
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: UserRemote) {
            with(binding) {
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                ivAvatar.loadImageWithGlide(contact.image.toString())
                ivContactRemove.setOnClickListener {
                    listener.onItemDeleteClick(contact.id)
                }
                clContactItem.setOnClickListener {
                    listener.onItemClick(bindingAdapterPosition, ivAvatar)
                }
                clContactItem.setOnLongClickListener {
                    listener.onItemLongClick(bindingAdapterPosition)
                    true
                }
            }
        }
    }

    inner class ContactSelectedViewHolder(private val binding: ItemContactSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: UserRemote) {
            with(binding) {
                chkSelect.isChecked = selectedPositions.contains(bindingAdapterPosition)
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                ivAvatar.loadImageWithGlide(contact.image.toString())
                clContactItem.setOnClickListener {
                    listener.onItemClick(bindingAdapterPosition)
                    chkSelect.toggle()
                }
            }
        }
    }

}
