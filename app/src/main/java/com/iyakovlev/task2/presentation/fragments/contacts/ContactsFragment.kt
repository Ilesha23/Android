package com.iyakovlev.task2.presentation.fragments.contacts

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.task2.R
import com.iyakovlev.task2.common.constants.Constants
import com.iyakovlev.task2.common.constants.Constants.CONTACT_ADDRESS
import com.iyakovlev.task2.common.constants.Constants.CONTACT_CAREER
import com.iyakovlev.task2.common.constants.Constants.CONTACT_NAME
import com.iyakovlev.task2.common.constants.Constants.CONTACT_PHOTO
import com.iyakovlev.task2.common.constants.Constants.TRANSITION_NAME
import com.iyakovlev.task2.common.constants.TestingConstants.isUsingTransactions
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.databinding.FragmentContactsBinding
import com.iyakovlev.task2.presentation.base.BaseFragment
import com.iyakovlev.task2.presentation.fragments.add_contact.AddContactDialogFragment
import com.iyakovlev.task2.presentation.fragments.contacts.adapters.ContactsAdapter
import com.iyakovlev.task2.presentation.fragments.contacts.interfaces.ContactItemClickListener
import com.iyakovlev.task2.presentation.fragments.detail_view.ContactDetailViewFragment
import com.iyakovlev.task2.presentation.utils.ItemSpacingDecoration
import com.iyakovlev.task2.presentation.utils.extentions.addSwipeToDelete
import com.iyakovlev.task2.presentation.utils.extentions.setButtonScrollUpListener
import com.iyakovlev.task2.utils.LOG_TAG
import kotlinx.coroutines.launch

class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModel: ContactsViewModel by viewModels()

    private val contactAdapter = ContactsAdapter(object : ContactItemClickListener {

        override fun onItemClick(position: Int, imageView: ImageView) {
            navigateToDetailView(position, imageView)
        }

        override fun onItemDeleteClick(position: Int) {
            deleteContactWithUndo(position)
        }

    })

    private fun deleteContactWithUndo(position: Int){
        viewModel.removeContact(position)
        showUndoDeleteSnackBar()
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setPhoneContactsList()
            } else {
                setFakeContactsList()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestContactsPermission()

        setupRecyclerView()
        setListeners()
        setObservers()

    }

    private fun requestContactsPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun setPhoneContactsList() {
        viewModel.loadContactsFromStorage(requireActivity().contentResolver)
    }

    private fun setFakeContactsList() {
        viewModel.createFakeContacts()
    }

    private fun setupRecyclerView() {
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.contacts_item_spacing)
            val lastSpacing = resources.getDimensionPixelSize(R.dimen.last_item_bottom_spacing)
            addItemDecoration(ItemSpacingDecoration(spacing, lastSpacing))

            this.setButtonScrollUpListener { isButtonVisible ->
                toggleFabVisibility(binding.fabUp, isButtonVisible)
            }

            addSwipeToDelete { position ->
                deleteContactWithUndo(position)
            }

        }
    }

    private fun setObservers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { newContactsList ->
                    contactAdapter.submitList(newContactsList)
                }
            }
        }

    }

    private fun setListeners() {

        with(binding) {

            btnAddContact.setOnClickListener {
                navigateOnAddContactDialog()
            }

            fabUp.setOnClickListener {
                rvContacts.smoothScrollToPosition(0)
            }
        }

    }

    private fun navigateOnAddContactDialog() {
        if (isUsingTransactions) {  //todo maybe class
            openAddContactDialog()
        } else {
            val action =
                ContactsFragmentDirections.actionContactsFragmentToAddContactDialogFragment(
                    viewModel   //todo !!!!!!!!! BAD !!!!!!!!!
                )
            navController.navigate(action)
        }
    }

    private fun navigateToDetailView(position: Int, imageView: ImageView) {
        // TRANSACTION
        if (isUsingTransactions) {
            navigateToDetailViewWithTransaction(position)
        } else {
            // NAVIGATION GRAPH
            navigateToDetailViewWithNavigation(position, imageView)
        }
    }

    private fun navigateToDetailViewWithTransaction(position: Int) {    //todo move inside adapter
        val fragment = ContactDetailViewFragment()
        val contact = contactAdapter.getContact(position)
        val bundle = Bundle().apply {
            putString(CONTACT_PHOTO, contact.photo)
            putString(CONTACT_NAME, contact.name)
            putString(CONTACT_CAREER, contact.career)
            putString(CONTACT_ADDRESS, contact.address)
        }
        fragment.arguments = bundle
        parentFragmentManager.commit {
            replace(R.id.nav_host_fragment, fragment)
            addToBackStack(null)
        }
        Log.e(LOG_TAG, "transaction")
    }

    private fun navigateToDetailViewWithNavigation(position: Int, imageView: ImageView) {
        val contact = contactAdapter.getContact(position)
        val contactId = contact.id.toString()
        val transitionName = "$TRANSITION_NAME$contactId"

        val action = ContactsFragmentDirections.actionContactsFragmentToContactDetailViewFragment(
            contactId = contact.id.toString(),
            contactPhoto = contact.photo,
            contactName = contact.name,
            contactCareer = contact.career,
            contactAddress = contact.address,
        )
        imageView.transitionName = transitionName
        val extras = FragmentNavigatorExtras(
            imageView to transitionName
        )
        navController.navigate(action, extras)
        Log.e(LOG_TAG, "navigated to $action")
    }


    private fun toggleFabVisibility(view: View, show: Boolean) { //todo extension
        val duration = 200L //todo const

        if (show) {
            view.visibility = View.VISIBLE
            view.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null)
                .start()
        } else {
            view.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.INVISIBLE
                    }
                })
                .start()
        }
    }

    private fun openAddContactDialog() {
        val dialogFragment = AddContactDialogFragment()
        dialogFragment.setViewModel(viewModel)

        dialogFragment.show(childFragmentManager, "TAG")
        Log.e(LOG_TAG, "dialog showed")
    }

    private fun showUndoDeleteSnackBar() {
        Snackbar.make(binding.root, R.string.contact_deleted_snackbar, Constants.SNACK_BAR_LENGTH)
            .setAction(R.string.undo_remove_snackbar) {
                viewModel.undoRemoveContact()
            }
            .show()
    }

}