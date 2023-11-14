package com.iyakovlev.contacts.presentation.fragments.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentContactsBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.fragments.contacts.adapters.ContactsAdapter
import com.iyakovlev.contacts.presentation.fragments.contacts.interfaces.ContactItemClickListener
import com.iyakovlev.contacts.presentation.utils.ItemSpacingDecoration
import com.iyakovlev.contacts.presentation.utils.extensions.showSnackBarWithTimer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModel: ContactsViewModel by viewModels()

    private var itemTouchHelper: ItemTouchHelper? = null

    private val contactAdapter = ContactsAdapter(object : ContactItemClickListener {

        override fun onItemClick(id: Long, imageView: ImageView) {
            navigateToDetailView(id, imageView)
        }

        override fun onItemDeleteClick(id: Long) {
            removeContactWithUndo(id)
        }

        override fun onItemLongClick(position: Int) {
            viewModel.changeSelectionState(true)
            viewModel.toggleSelectedPosition(position)
//            toggleSwipeToDelete(false)
        }

        override fun onItemClick(position: Int) {
            viewModel.toggleSelectedPosition(position)
        }

    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleLoading(true)
        setupRecyclerView()
        setListeners()
        setObservers()

    }

//    private fun toggleSwipeToDelete(isActive: Boolean) {
//        if (!isActive) {
//            itemTouchHelper?.attachToRecyclerView(
//                if (!viewModel.isMultiSelect.value) binding.rvContacts else null
//            )
//        } else {
//            itemTouchHelper?.attachToRecyclerView(binding.rvContacts)
//        }
//    }

    private fun removeContactWithUndo(id: Long) {
        viewModel.deleteContact(id)
        showUndoDeleteSnackBar(getString(R.string.contact_deleted_snackbar)) {
            viewModel.undoRemoveContact()
        }
    }

    private fun removeContactListWithUndo() {
        viewModel.removeSelectedContacts()
        showUndoDeleteSnackBar(getString(R.string.contact_list_deleted_snackbar)) {
            viewModel.undoRemoveContactsList()
        }
    }

    private fun makeBinButton() {
        binding.rvContacts.clearOnScrollListeners()
        val layoutManager = binding.rvContacts.layoutManager as LinearLayoutManager
        val firstItem = layoutManager.findFirstVisibleItemPosition()
        binding.fabUp.icon = ContextCompat.getDrawable(requireContext(), R.drawable.fab_bin)
        binding.fabUp.visibility = View.VISIBLE
        if (firstItem == 0) {
            binding.rvContacts.smoothScrollBy(
                0,
                6
            ) // bad decision, but i don't know how to fix that
        }
    }

    private fun makeUpButton() {
        binding.fabUp.icon =
            ContextCompat.getDrawable(requireContext(), R.drawable.floating_action_button_up)
//        val layoutManager = binding.rvContacts.layoutManager as LinearLayoutManager
//        val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
//        if (firstItem == 0) {
//            binding.fabUp.visibility = View.INVISIBLE
//        } else {
//            binding.fabUp.visibility = View.VISIBLE
//        }
    }

    private fun setupRecyclerView() {
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.contacts_item_spacing)
            val lastSpacing = resources.getDimensionPixelSize(R.dimen.last_item_bottom_spacing)
            if (itemDecorationCount == 0) {
                addItemDecoration(ItemSpacingDecoration(spacing, lastSpacing))
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = binding.rvContacts.layoutManager as LinearLayoutManager
                    val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (firstItem == 0) {
                        binding.fabUp.hide()
                    }
                    if (dy > 0) {
                        binding.fabUp.show()
                    }
                }
            })

//            setButtonScrollListener { isButtonVisible ->
//                if (!viewModel.isMultiSelect.value) {
//                    log("contacts - $binding", ISDEBUG)
//                    binding.fabUp.toggleFabVisibility(FAB_ANIMATION_TIME, isButtonVisible)
//                } else {
//                    binding.fabUp.toggleFabVisibility(FAB_ANIMATION_TIME, true)
//                }
//            }

//            itemTouchHelper = addSwipeToDelete { position ->
//                removeContactWithUndo(position) todo
//            }
        }
    }

    private fun setObservers() {
        viewModel.updateContacts()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { list ->
                        contactAdapter.submitList(list.data)
                        if (viewModel.state.value is Resource.Error<*>) {
                            toggleLoading(false)
                            Toast.makeText(context, viewModel.state.value.message, Toast.LENGTH_SHORT).show()
                        } else if (viewModel.state.value is Resource.Success) {
                            toggleLoading(false)
                        }
                    }
                }
                launch {
                    viewModel.isMultiSelect.collect {
                        contactAdapter.changeSelectionState(it)
                        if (it) {
                            makeBinButton()
//                            toggleSwipeToDelete(false) todo
                        } else {
                            makeUpButton()
//                            toggleSwipeToDelete(true) todo
                        }
                    }
                }
                launch {
                    viewModel.selectedPositions.collect {
                        contactAdapter.changeSelectedPositions(it)
                    }
                }
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            btnAddContact.setOnClickListener {
//                navigateToAddContactDialog()
                navController.navigate(ContactsFragmentDirections.actionContactsFragmentToAddContactFragment())
//                navController.navigate(R.id.addContactFragment)
            }
            fabUp.setOnClickListener {
                if (!viewModel.isMultiSelect.value) {
                    rvContacts.smoothScrollToPosition(0)
                    binding.fabUp.hide()
                } else {
                    removeContactListWithUndo()
                    contactAdapter.changeSelectedPositions(emptyList())
                    viewModel.changeSelectionState(false)

                }
            }
            ibBack.setOnClickListener {
                navController.navigateUp() // TODO:  
//                navController.navigate(ContactsFragmentDirections.actionContactsFragmentToMainFragment())
            }
        }
    }


    private fun navigateToDetailView(id: Long, imageView: ImageView) {
        val contact = viewModel.state.value.data?.find {
            it.id == id
        }
        navController.navigate(ContactsFragmentDirections.actionContactsFragmentToContactDetailViewFragment(
            contact?.image ?: "",
            contact?.name ?: getString(R.string.default_name_main),
            contact?.career ?: getString(R.string.career_placeholder),
            contact?.address ?: getString(R.string.address)
        ))
    }

    // TODO:
//    private fun navigateToDetailViewWithNavigation(position: Int, imageView: ImageView) {
//        val contact = viewModel.getContact(position)
//        val contactId = contact.id.toString()
//        val transitionName = "$TRANSITION_NAME$contactId"
//
//        val action = ContactsFragmentDirections.actionContactsFragmentToContactDetailViewFragment(
//            contactId = contact.id.toString(),
//            contactPhoto = contact.photo,
//            contactName = contact.name,
//            contactCareer = contact.career,
//            contactAddress = contact.address,
//        )
//        imageView.transitionName = transitionName
//        val extras = FragmentNavigatorExtras(
//            imageView to transitionName
//        )
//        navController.navigate(action, extras)
//        log("navigated to $action", isDebug)
//    }

    @SuppressLint("ShowToast")
    private fun showUndoDeleteSnackBar(message: String, action: () -> Unit) {
        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .showSnackBarWithTimer(getString(R.string.undo_remove_snackbar)) {
                action()
            }
    }

    private fun toggleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbContacts.visibility = View.VISIBLE
            } else {
                pbContacts.visibility = View.GONE
            }
        }
    }
    
}