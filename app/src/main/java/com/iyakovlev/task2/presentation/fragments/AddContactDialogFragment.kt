package com.iyakovlev.task2.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.AddContactDialogBinding
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.domain.ContactsViewModel
import com.iyakovlev.task2.utils.loadImageWithGlide
import java.util.UUID


class AddContactDialogFragment : AppCompatDialogFragment() {

    private var _binding: AddContactDialogBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ContactsViewModel by viewModels({ requireActivity() })

    private lateinit var photoActivityResult: ActivityResultLauncher<Intent>
    private var contact = Contact(UUID.randomUUID(), null, "", "")

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        photoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val photo = it.data?.data.toString()
                contact = Contact(contact.id, photo, contact.name, contact.career)
                binding.ivAddContactAvatar.loadImageWithGlide(photo)
            }
        }
    }


    private fun setupListeners() {
        with(binding) {
            btnLoadImage.setOnClickListener {
                openGallery()
            }
            btnSave.setOnClickListener {
                val name = etUsername.text.toString()
                val career = etCareer.text.toString()
                contact = Contact(contact.id, contact.photo, name, career)
                viewModel.addContact(contact)
                dismiss()
            }
            ibBack.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        photoActivityResult.launch(intent)
    }

}