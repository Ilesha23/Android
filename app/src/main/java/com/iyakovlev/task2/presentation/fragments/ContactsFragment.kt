package com.iyakovlev.task2.presentation.fragments

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.FragmentContactsBinding
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.domain.ContactsAdapter
import com.iyakovlev.task2.domain.ContactsViewModel
import com.iyakovlev.task2.presentation.common.BaseFragment
import com.iyakovlev.task2.presentation.fragments.interfaces.ContactItemClickListener
import com.iyakovlev.task2.utils.Constants
import com.iyakovlev.task2.utils.Constants.CONTACT_ADDRESS
import com.iyakovlev.task2.utils.Constants.CONTACT_CAREER
import com.iyakovlev.task2.utils.Constants.CONTACT_NAME
import com.iyakovlev.task2.utils.Constants.CONTACT_PHOTO
import com.iyakovlev.task2.utils.Constants.IS_FIRST_LAUNCH
import com.iyakovlev.task2.utils.Constants.LOG_TAG
import com.iyakovlev.task2.utils.Constants.PREFERENCES
import com.iyakovlev.task2.utils.Constants.READ_CONTACTS_PERMISSION_KEY
import com.iyakovlev.task2.utils.Constants.TRANSITION_NAME
import com.iyakovlev.task2.utils.ItemSpacingDecoration
import com.iyakovlev.task2.utils.TestingConstants.isUsingTransactions
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModel: ContactsViewModel by viewModels()
    private val contactAdapter = ContactsAdapter(object : ContactItemClickListener {

        override fun onItemClick(position: Int, imageView: ImageView) {
            navigateToDetailView(position, imageView)
        }

        override fun onItemDeleteClick(position: Int) {
            viewModel.removeContact(position)
            showUndoDeleteSnackBar()
        }

    })


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.loadContactsFromStorage(requireActivity().contentResolver)
                val prefs = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean(READ_CONTACTS_PERMISSION_KEY, true)
                editor.apply()
            } else {
                viewModel.createFakeContacts()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestContactsPermission()

        setupRecyclerView()
        setObservers()
        setListeners()
        addSwipeToDelete()

    }

    private fun requestContactsPermission() {
        val prefs = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        if (prefs.getBoolean(READ_CONTACTS_PERMISSION_KEY, false)) {
            viewModel.loadContactsFromStorage(requireActivity().contentResolver)
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
        lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
//                contactAdapter.setContacts(contacts)
                contactAdapter.submitList(viewModel.contacts.value.toMutableList())
            }
        }
    }

    override fun setListeners() {
        binding.btnAddContact.setOnClickListener {
            if (isUsingTransactions) {
                openAddContactDialog()
            } else {
                val action = ContactsFragmentDirections
                    .actionContactsFragmentToAddContactDialogFragment(viewModel)
                navController.navigate(action)
            }
        }
        binding.rvContacts.viewTreeObserver.addOnScrollChangedListener {
            checkButtonUp()
        }
        binding.fabUp.setOnClickListener {
            binding.rvContacts.smoothScrollToPosition(0)
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
        val fragment = ContactDetailViewFragment.newInstance()
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
                val contact: Contact? = viewModel.getContact(index)

                if (contact != null) {
                    viewModel.removeContact(contact)
                    showUndoDeleteSnackBar()
                }
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ContactsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}