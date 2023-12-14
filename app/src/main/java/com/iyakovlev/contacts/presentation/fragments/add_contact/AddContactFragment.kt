package com.iyakovlev.contacts.presentation.fragments.add_contact

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentAddContactBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.fragments.add_contact.adapters.UsersAdapter
import com.iyakovlev.contacts.presentation.fragments.add_contact.interfaces.UserItemClickListener
import com.iyakovlev.contacts.presentation.utils.ItemSpacingDecoration
import com.iyakovlev.contacts.utils.log
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

        toggleLoading(true)
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
            }
            svUsers.setOnSearchClickListener {
                tvHeader.visibility = View.INVISIBLE
                ibBack.visibility = View.INVISIBLE
                it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            svUsers.setOnCloseListener {
                tvHeader.visibility = View.VISIBLE
                ibBack.visibility = View.VISIBLE
                svUsers.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                viewModel.setFilter(null)
                userAdapter.submitList(viewModel.state.value.data)
                false
            }
            svUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    viewModel.setFilter(p0)
                    log("setted filter: $p0", Constants.ISDEBUG)
                    return true
                }

            })
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        userAdapter.submitList(it.data)
                        if (viewModel.state.value is Resource.Error<*>) {
                            Toast.makeText(
                                context,
                                getString(
                                    viewModel.state.value.message ?: R.string.error_contact_add
                                ),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            toggleLoading(false)
                        }
                        if (viewModel.state.value is Resource.Success) {
                            toggleLoading(false)
                        }
                    }
                }
                launch {
                    viewModel.selectedContacts.collect {
                        userAdapter.changeSelectedPositions(viewModel.selectedContacts.value)
                        userAdapter.submitList(viewModel.state.value.data)
                    }
                }
                launch {
                    viewModel.cachedList.collect {
                        userAdapter.submitList(it)
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

    private fun toggleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbAddContacts.visibility = View.VISIBLE
            } else {
                pbAddContacts.visibility = View.GONE
            }
        }
    }

}