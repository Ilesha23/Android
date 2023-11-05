package com.iyakovlev.contacts.presentation.fragments.add_contact

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentAddContactBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.fragments.add_contact.adapters.UsersAdapter
import com.iyakovlev.contacts.presentation.fragments.add_contact.interfaces.UserItemClickListener
import com.iyakovlev.contacts.presentation.utils.ItemSpacingDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFragment :
    BaseFragment<FragmentAddContactBinding>(FragmentAddContactBinding::inflate) {

    private val viewModel: AddContactViewModel by viewModels()
    private val userAdapter: UsersAdapter = UsersAdapter(object : UserItemClickListener {
        override fun onItemClick(id: Long) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.addContact(id)
                viewModel.toggleSelectedContact(id)
            }
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setListeners()
        setObservers()

    }

    private fun setListeners() {
        with(binding) {
            fab.setOnClickListener {
                rvUsers.scrollToPosition(0)
            }
            ibBack.setOnClickListener {
                navController.navigateUp()
//                navController.navigate(AddContactFragmentDirections.actionAddContactFragmentToContactsFragment())
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        userAdapter.submitList(it.data)
//                        userAdapter.changeContacts(it.data)
                        if (viewModel.state.value is Resource.Error<*>) {
                            Toast.makeText(context, viewModel.state.value.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                launch {
                    viewModel.selectedContacts.collect {
                        userAdapter.changeSelectedPositions(viewModel.selectedContacts.value)
                        userAdapter.submitList(viewModel.state.value.data)
                    }
                }
            }
        }
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

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = binding.rvUsers.layoutManager as LinearLayoutManager
                    val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (firstItem == 0) {
                        binding.fab.hide()
                    }
                    if (dy > 0) {
                        binding.fab.show()
                    }
                }
            })
        }
    }

}