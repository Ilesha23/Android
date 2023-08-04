package com.iyakovlev.task2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.iyakovlev.task2.R
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.domain.ContactsViewModel
import com.iyakovlev.task2.databinding.AddContactDialogBinding
import java.util.UUID


class AddContactDialogFragment : AppCompatDialogFragment() {

    private var _binding: AddContactDialogBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ContactsViewModel by viewModels({requireActivity()})

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            btnSave.setOnClickListener {
//                Toast.makeText(context, "*saved*", Toast.LENGTH_SHORT).show()
                var contact: Contact
                with(binding) {
                    val name = etUsername.text.toString()
                    val career = etCareer.text.toString()
                    // TODO: photo
                    contact = Contact(UUID.randomUUID(), null, name, career)
                }
                viewModel.addContact(contact)
                dismiss()
            }
            ibBack.setOnClickListener {
                dismiss()
            }
        }
    }

}