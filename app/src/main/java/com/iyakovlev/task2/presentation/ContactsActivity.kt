package com.iyakovlev.task2.presentation

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.task2.R
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.domain.ContactsViewModel
import com.iyakovlev.task2.databinding.ActivityContactsBinding
import com.iyakovlev.task2.domain.ContactsAdapter
import com.iyakovlev.task2.utils.Constants.IS_USER_ASKED_KEY
import com.iyakovlev.task2.utils.Constants.LOG_TAG
import com.iyakovlev.task2.utils.Constants.READ_CONTACTS_PERMISSION_REQUEST
import com.iyakovlev.task2.utils.Constants.SNACK_BAR_LENGTH
import com.iyakovlev.task2.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch


class ContactsActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    private val vm: ContactsViewModel by viewModels()
    private val contactAdapter = ContactsAdapter()
    private var isUserAsked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e(LOG_TAG, "activity created")

        isUserAsked = savedInstanceState?.getBoolean(IS_USER_ASKED_KEY) ?: false
        if (!isUserAsked) {
            requestContactsReadPermission()
        }

        setupRecyclerView()
        lifecycleScope.launch {
            observeContacts()
        }

        addSwipeToDelete()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_USER_ASKED_KEY, isUserAsked)
        super.onSaveInstanceState(outState)
    }

    private fun requestContactsReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST
            )
            isUserAsked = true
            Log.e(LOG_TAG, "permission to read contacts asked")
        } else {
            vm.loadContactsFromStorage(contentResolver)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() and (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show() // TODO:
                Log.e(LOG_TAG, "permission to read contacts granted")

                vm.loadContactsFromStorage(contentResolver)

            } else {
                Toast.makeText(this, "not granted", Toast.LENGTH_SHORT).show() // TODO:
                Log.e(LOG_TAG, "permission to read contacts not granted")

                vm.createDefaultContacts()

            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            adapter = contactAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.contacts_item_spacing)
            val lastSpacing = resources.getDimensionPixelSize(R.dimen.last_item_bottom_spacing)
            addItemDecoration(ItemSpacingDecoration(spacing, lastSpacing))
        }
    }

    private suspend fun observeContacts() {
        vm.contacts.collect { contacts ->
            contactAdapter.setContacts(contacts)
        }
    }

    override fun setListeners() {
        contactAdapter.setOnRemoveClickListener { contact ->
            vm.removeContact(contact)
            showUndoDeleteSnackBar()
        }
        binding.btnAddContact.setOnClickListener {
            openAddContactDialog()
        }
        binding.rvContacts.viewTreeObserver.addOnScrollChangedListener {
            checkButtonUp()
        }
        binding.fabUp.setOnClickListener {
            binding.rvContacts.smoothScrollToPosition(0)
        }
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

        dialogFragment.show(supportFragmentManager, "TAG") // TODO:
        Log.e(LOG_TAG, "dialog showed")
    }

    private fun showUndoDeleteSnackBar() {
        Snackbar.make(binding.root, R.string.contact_deleted_snackbar, SNACK_BAR_LENGTH)
            .setAction(R.string.undo_remove_snackbar) {
                vm.undoRemoveContact()
            }
            .show()
    }

    private fun addSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.END or ItemTouchHelper.START) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            )
                    : Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.bindingAdapterPosition
                val contact: Contact? = vm.getContact(index)

                if (contact != null) {
                    vm.removeContact(contact)
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

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }).attachToRecyclerView(binding.rvContacts)
    }

}