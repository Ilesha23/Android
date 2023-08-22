package com.iyakovlev.task2.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
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

    private var onItemClicked: ((position: Int, imageView: ImageView) -> Unit)? = null
    private var onDeleteClicked: ((position: Int) -> Unit)? = null
    fun setOnItemClickedListener(listener: (position: Int, imageView: ImageView) -> Unit) {
        onItemClicked = listener
    }

    fun setOnDeleteClickedListener(listener: (position: Int) -> Unit) {
        onDeleteClicked = listener
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

    fun getContact(pos: Int): Contact = contacts[pos]

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position], onItemClicked, onDeleteClicked)
    }

    class ContactViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact,
                 onItemClicked: ((position: Int, imageView: ImageView) -> Unit)?,
                 onDeleteClicked: ((position: Int) -> Unit)?
        ) {
            binding.apply {
//                ivAvatar.transitionName = "contactImageTransition_list_${contact.id.toString()}"
                tvContactName.text = contact.name
                tvContactCareer.text = contact.career
                if (contact.photo.isNotBlank()) {
                    binding.ivAvatar.loadImageWithGlide(contact.photo)
                } else {
                    Glide.with(ivAvatar.context).clear(ivAvatar)
                    ivAvatar.setImageResource(R.drawable.baseline_person_24)
                }
                ivContactRemove.setOnClickListener {
                    onDeleteClicked?.invoke(bindingAdapterPosition)
                }
                clContactItem.setOnClickListener {
                    onItemClicked?.invoke(bindingAdapterPosition, binding.ivAvatar)
                }
            }
        }
    }

}