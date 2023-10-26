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
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentContactsBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.fragments.add_contact.adapters.UsersAdapter
import com.iyakovlev.contacts.presentation.fragments.contacts.adapters.ContactsAdapter
import com.iyakovlev.contacts.presentation.fragments.contacts.interfaces.ContactItemClickListener
import com.iyakovlev.contacts.presentation.utils.ItemSpacingDecoration
import com.iyakovlev.contacts.presentation.utils.extensions.addSwipeToDelete
import com.iyakovlev.contacts.presentation.utils.extensions.setButtonScrollListener
import com.iyakovlev.contacts.presentation.utils.extensions.showSnackBarWithTimer
import com.iyakovlev.contacts.presentation.utils.extensions.toggleFabVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModel: ContactsViewModel by viewModels()

    private var itemTouchHelper: ItemTouchHelper? = null

    private val contactAdapter = ContactsAdapter(object : ContactItemClickListener {

        override fun onItemClick(position: Int, imageView: ImageView) {
//            navigateToDetailView(position, imageView) todo
        }

        override fun onItemDeleteClick(position: Int) {
//            removeContactWithUndo(position) todo
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

//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                setPhoneContactsList()
//            } else {
//                setFakeContactsList()
//            }
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requestContactsPermission()

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

//    private fun removeContactWithUndo(position: Int) {
//        viewModel.removeContact(position)
//        showUndoDeleteSnackBar(getString(R.string.contact_deleted_snackbar)) {
//            viewModel.undoRemoveContact()
//        }
//    }

//    private fun removeContactListWithUndo() {
//        viewModel.removeSelectedContacts()
//        showUndoDeleteSnackBar(getString(R.string.contact_list_deleted_snackbar)) {
//            viewModel.undoRemoveContactsList()
//        }
//    }

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
        val layoutManager = binding.rvContacts.layoutManager as LinearLayoutManager
        val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (firstItem == 0) {
            binding.fabUp.visibility = View.INVISIBLE
        } else {
            binding.fabUp.visibility = View.VISIBLE
        }
    }

//    private fun setPhoneContactsList() {
//        viewModel.loadContactsFromStorage()
//        val prefs = requireContext().getSharedPreferences(
//            PREFERENCES,
//            Context.MODE_PRIVATE
//        )
//        val editor = prefs.edit()
//        editor.putBoolean(READ_CONTACTS_PERMISSION_KEY, true)
//        editor.apply()
//    }

//    private fun setFakeContactsList() {
//        viewModel.createFakeContacts()
//    }

    // bad function to make program not ask permission twice
//    private fun requestContactsPermission() {
//        val prefs = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//
//        if (prefs.getBoolean(READ_CONTACTS_PERMISSION_KEY, false)) {
//            viewModel.loadContactsFromStorage()
//        } else {
//            if (prefs.getBoolean(
//                    IS_FIRST_LAUNCH,
//                    true
//                )
//            ) { // tracks apps permission request to store it forever
//                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
//                editor.putBoolean(IS_FIRST_LAUNCH, false)
//                editor.apply()
//            } else {
//                viewModel.createFakeContacts()
//            }
//        }
//    }


    private fun setupRecyclerView() {
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.contacts_item_spacing)
            val lastSpacing = resources.getDimensionPixelSize(R.dimen.last_item_bottom_spacing)
            if (itemDecorationCount == 0) {
                addItemDecoration(ItemSpacingDecoration(spacing, lastSpacing))
            }

            setButtonScrollListener { isButtonVisible ->
                if (!viewModel.isMultiSelect.value) {
                    binding.fabUp.toggleFabVisibility(FAB_ANIMATION_TIME, isButtonVisible)
                } else {
                    binding.fabUp.toggleFabVisibility(FAB_ANIMATION_TIME, true)
                }
            }

            itemTouchHelper = addSwipeToDelete { position ->
//                removeContactWithUndo(position) todo
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch(Dispatchers.Main) {
                    viewModel.state.collect { newContactsList ->
                        contactAdapter.submitList(newContactsList.data)
                        if (viewModel.state.value is Resource.Error<*>) {
                            Toast.makeText(context, viewModel.state.value.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                launch(Dispatchers.Main) {
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
                launch(Dispatchers.Main) {
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
            }
            fabUp.setOnClickListener {
                if (!viewModel.isMultiSelect.value) {
                    rvContacts.smoothScrollToPosition(0)
                } else {
//                    removeContactListWithUndo() todo
                }
            }
            ibBack.setOnClickListener {
                navController.navigateUp()
            }
        }
    }

    // TODO:
//    private fun navigateToAddContactDialog() {
//        Log.e("TAG", viewModel.toString())
//        val action = ContactsFragmentDirections
//            .actionContactsFragmentToAddContactDialogFragment()
//        navController.navigate(action)
//    }

    // TODO:
//    private fun navigateToDetailView(position: Int, imageView: ImageView) {
//        navigateToDetailViewWithNavigation(position, imageView)
//    }

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

    companion object {

//        const val READ_CONTACTS_PERMISSION_KEY = "READ_CONTACTS_PERMISSION_KEY"
//        const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"

        const val FAB_ANIMATION_TIME = 200L

    }
}