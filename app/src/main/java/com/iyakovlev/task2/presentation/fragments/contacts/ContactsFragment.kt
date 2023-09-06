package com.iyakovlev.task2.presentation.fragments.contacts

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
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
import com.iyakovlev.task2.databinding.FragmentContactsBinding
import com.iyakovlev.task2.presentation.base.BaseFragment
import com.iyakovlev.task2.presentation.fragments.add_contact.AddContactDialogFragment
import com.iyakovlev.task2.presentation.fragments.contacts.adapters.ContactsAdapter
import com.iyakovlev.task2.presentation.fragments.contacts.interfaces.ContactItemClickListener
import com.iyakovlev.task2.presentation.fragments.detail_view.ContactDetailViewFragment
import com.iyakovlev.task2.presentation.utils.ItemSpacingDecoration
import com.iyakovlev.task2.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val isDebug = false

    private val viewModel: ContactsViewModel by viewModels()

    private val contactAdapter = ContactsAdapter(object : ContactItemClickListener {

        override fun onItemClick(position: Int, imageView: ImageView) {
            navigateToDetailView(position, imageView)
        }

        override fun onItemDeleteClick(position: Int) {
            deleteContactWithUndo(position)
        }

    })

    private fun deleteContactWithUndo(position: Int) {
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
        addSwipeToDelete() // TODO:

    }

    private fun setPhoneContactsList() {
        viewModel.loadContactsFromStorage(/*contentResolver*/)
        val prefs = requireContext().getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean(READ_CONTACTS_PERMISSION_KEY, true)
        editor.apply()
    }

    private fun setFakeContactsList() {
        viewModel.createFakeContacts()
    }

    private fun requestContactsPermission() {
        val prefs = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        if (prefs.getBoolean(READ_CONTACTS_PERMISSION_KEY, false)) {
            viewModel.loadContactsFromStorage(/*contentResolver*/)
        } else {
            if (prefs.getBoolean(
                    IS_FIRST_LAUNCH,
                    true
                )
            ) { // tracks apps permission request to store it forever
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                editor.putBoolean(IS_FIRST_LAUNCH, false)
                editor.apply()
            } else {
                viewModel.createFakeContacts()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.contacts_item_spacing)
            val lastSpacing = resources.getDimensionPixelSize(R.dimen.last_item_bottom_spacing)
            addItemDecoration(ItemSpacingDecoration(spacing, lastSpacing))
        }
    }

    override fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { newContactsList ->
                    contactAdapter.submitList(newContactsList)
                }
            }
        }
    }

    override fun setListeners() {
        with(binding) {
            btnAddContact.setOnClickListener {
                navigateToAddContactDialog()
            }
            rvContacts.viewTreeObserver.addOnScrollChangedListener {
                checkButtonUp()
            }
            fabUp.setOnClickListener {
                rvContacts.smoothScrollToPosition(0)
            }
        }
    }

    private fun navigateToAddContactDialog() {
        if (isUsingTransactions) {
            openAddContactDialog()
        } else {
            Log.e("TAG", viewModel.toString())
            val action = ContactsFragmentDirections
                .actionContactsFragmentToAddContactDialogFragment()
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

    private fun navigateToDetailViewWithTransaction(position: Int) {
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
        log("transaction", isDebug)
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
        log("navigated to $action", isDebug)
    }

    private fun checkButtonUp() {
        val layoutManager = binding.rvContacts.layoutManager as LinearLayoutManager
        val firstItem = layoutManager.findFirstVisibleItemPosition()
        if (firstItem > 0) {
            toggleFabVisibility(binding.fabUp, true)
        } else {
            toggleFabVisibility(binding.fabUp, false)
        }
    }

    private fun toggleFabVisibility(view: View, show: Boolean) {
        val duration = 200L

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
        Log.e("TAG", viewModel.toString())

        dialogFragment.show(childFragmentManager, "TAG")
        log("dialog showed", isDebug)
    }

    private fun showUndoDeleteSnackBar() {
        Snackbar.make(binding.root, R.string.contact_deleted_snackbar, Constants.SNACK_BAR_LENGTH)
            .setAction(R.string.undo_remove_snackbar) {
                viewModel.undoRemoveContact()
            }
            .show()
    }

    private fun addSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.END or ItemTouchHelper.START
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            )
                    : Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.bindingAdapterPosition
                viewModel.removeContact(index)
                showUndoDeleteSnackBar()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val paint = Paint()
                paint.color = Color.RED

                val background: RectF = if (dX > 0) {
                    RectF(
                        viewHolder.itemView.left.toFloat(),
                        viewHolder.itemView.top.toFloat(),
                        viewHolder.itemView.left.toFloat() + dX,
                        viewHolder.itemView.bottom.toFloat()
                    )
                } else {
                    RectF(
                        viewHolder.itemView.right.toFloat() + dX,
                        viewHolder.itemView.top.toFloat(),
                        viewHolder.itemView.right.toFloat(),
                        viewHolder.itemView.bottom.toFloat()
                    )
                }
                c.drawRect(background, paint)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.rvContacts)
    }

    companion object {

        const val PREFERENCES = "PREFERENCES"
        const val READ_CONTACTS_PERMISSION_KEY = "READ_CONTACTS_PERMISSION_KEY"
        const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"

    }
}