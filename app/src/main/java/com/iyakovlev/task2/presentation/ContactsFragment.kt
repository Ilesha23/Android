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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.FragmentContactsBinding
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.domain.ContactsAdapter
import com.iyakovlev.task2.domain.ContactsViewModel
import com.iyakovlev.task2.utils.Constants
import com.iyakovlev.task2.utils.Constants.IS_USER_ASKED_KEY
import com.iyakovlev.task2.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    private val vm: ContactsViewModel by viewModels()
    private val contactAdapter = ContactsAdapter()
    private var isUserAsked = false
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                vm.loadContactsFromStorage(requireActivity().contentResolver)
            } else {
                vm.createDefaultContacts()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isUserAsked = savedInstanceState?.getBoolean(IS_USER_ASKED_KEY) ?: false
        if (!isUserAsked) {
            requestContactsPermission()
        }

        setupRecyclerView()
        setObservers()
        setListeners()
        addSwipeToDelete()

    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_USER_ASKED_KEY, isUserAsked)
        super.onSaveInstanceState(outState)
    }

    private fun requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isUserAsked = true
            vm.loadContactsFromStorage(requireActivity().contentResolver)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
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

    private suspend fun observeContacts() {
        vm.contacts.collect { contacts ->
            contactAdapter.setContacts(contacts)
        }
    }

    override fun setListeners() {
        contactAdapter.setOnDeleteClickedListener { position ->
            vm.removeContact(position)
            showUndoDeleteSnackBar()
        }
        contactAdapter.setOnItemClickedListener { position ->
            Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
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

        dialogFragment.show(childFragmentManager, "TAG") // TODO:
        Log.e(Constants.LOG_TAG, "dialog showed")
    }

    private fun showUndoDeleteSnackBar() {
        Snackbar.make(binding.root, R.string.contact_deleted_snackbar, Constants.SNACK_BAR_LENGTH)
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

    override fun setObservers() {
        lifecycleScope.launch {
            observeContacts()
        }
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
        fun newInstance(/*param1: String, param2: String*/) =
            ContactsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}