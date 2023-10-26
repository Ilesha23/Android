package com.iyakovlev.contacts.presentation.fragments.add_contact

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.databinding.FragmentAddContactBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.fragments.contacts.ContactsFragment
import com.iyakovlev.contacts.presentation.fragments.add_contact.adapters.UsersAdapter
import com.iyakovlev.contacts.presentation.fragments.add_contact.interfaces.UserItemClickListener
import com.iyakovlev.contacts.presentation.utils.ItemSpacingDecoration
import com.iyakovlev.contacts.presentation.utils.extensions.setButtonScrollListener
import com.iyakovlev.contacts.presentation.utils.extensions.toggleFabVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : BaseFragment<FragmentAddContactBinding>(FragmentAddContactBinding::inflate) {

    private val viewModel: AddContactViewModel by viewModels()
    private val userAdapter: UsersAdapter = UsersAdapter(object: UserItemClickListener{
        override fun onItemClick(position: Int, imageView: ImageView) {
            TODO("Not yet implemented")
        }

        override fun onItemClick(position: Int) {
            TODO("Not yet implemented")
        }

        override fun onItemDeleteClick(position: Int) {
            TODO("Not yet implemented")
        }

        override fun onItemLongClick(position: Int) {
            TODO("Not yet implemented")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRecyclerView()
        setListeners()
        setObservers()

    }

    private fun setListeners() {

    }

    private fun setObservers() {

    }

    private fun setupRecyclerView() {
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.contacts_item_spacing)
            val lastSpacing = resources.getDimensionPixelSize(R.dimen.last_item_bottom_spacing)
            if (itemDecorationCount == 0) {
                addItemDecoration(ItemSpacingDecoration(spacing, lastSpacing))
            }

            setButtonScrollListener { isButtonVisible ->
                binding.fabUp.toggleFabVisibility(ContactsFragment.FAB_ANIMATION_TIME, true)
            }
        }
    }

}