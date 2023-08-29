package com.iyakovlev.task4.presentation.fragments

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.task4.R
import com.iyakovlev.task4.databinding.FragmentContactsBinding
import com.iyakovlev.task4.domain.Contact
import com.iyakovlev.task4.presentation.adapters.ContactsAdapter
import com.iyakovlev.task4.domain.ContactsViewModel
import com.iyakovlev.task4.presentation.common.BaseFragment
import com.iyakovlev.task4.presentation.fragments.interfaces.ContactItemClickListener
import com.iyakovlev.task4.utils.Constants
import com.iyakovlev.task4.utils.Constants.IS_FIRST_LAUNCH
import com.iyakovlev.task4.utils.Constants.LOG_TAG
import com.iyakovlev.task4.utils.Constants.PREFERENCES
import com.iyakovlev.task4.utils.Constants.READ_CONTACTS_PERMISSION_KEY
import com.iyakovlev.task4.utils.Constants.TRANSITION_NAME
import com.iyakovlev.task4.utils.ItemSpacingDecoration
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

        override fun onItemLongClick(position: Int) {
//            enterSelectionMode()
            Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
            Log.e(LOG_TAG, "$position")
        }

        override fun onItemAddToSelection(position: Int) {
            viewModel.toggleSelection(position)
        }

    })

//    private fun enterSelectionMode() {
//        contactAdapter.toggleSelectionMode()
//        contactAdapter.submitList(viewModel.contacts.value.toMutableList())
//    }


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
                contactAdapter.submitList(contacts.toMutableList())
            }
        }
    }

    override fun setListeners() {
        binding.btnAddContact.setOnClickListener {
            val action = ViewPagerFragmentDirections
                .actionViewPagerFragmentToAddContactDialogFragment(viewModel)
            navController.navigate(action)
        }
        binding.rvContacts.viewTreeObserver.addOnScrollChangedListener {
            if (!viewModel.isSelectionMode()) {
                checkButtonUp()
            }
        }
        binding.fabUp.setOnClickListener {
            if (viewModel.isSelectionMode()) {
                binding.fabUp.visibility = View.VISIBLE
                binding.fabUp.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
                viewModel.removeSelectedContacts()
            } else {
                binding.fabUp.visibility = View.INVISIBLE
                binding.rvContacts.smoothScrollToPosition(0)
            }
        }
    }

    private fun navigateToDetailView(position: Int, imageView: ImageView) {
        val contact = contactAdapter.getContact(position)
        val contactId = contact.id.toString()
        val transitionName = "$TRANSITION_NAME$contactId"

        val action = ViewPagerFragmentDirections.actionViewPagerFragmentToContactDetailViewFragment(
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
                val index = viewHolder.adapterPosition
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

}