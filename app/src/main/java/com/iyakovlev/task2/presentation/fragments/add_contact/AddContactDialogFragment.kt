package com.iyakovlev.task2.presentation.fragments.add_contact

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.task2.R
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.databinding.AddContactDialogBinding
import com.iyakovlev.task2.presentation.utils.extensions.loadImageWithGlide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID


@AndroidEntryPoint
class AddContactDialogFragment : AppCompatDialogFragment() {

    private val viewModel: AddContactViewModel by viewModels()

    private var _binding: AddContactDialogBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var photoActivityResult: ActivityResultLauncher<Intent>
    private var contact = Contact(UUID.randomUUID(), "", "", "", "")

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

        setListeners()
        setObservers()

        photoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val photo = viewModel.photo.value.ifBlank { it.data?.data.toString() }
                viewModel.setPhoto(photo)
                contact = Contact(contact.id, photo, contact.name, contact.career, contact.address)
                binding.ivAddContactAvatar.loadImageWithGlide(photo)
            }
        }

    }

    private fun setListeners() {
        with(binding) {
            btnLoadImage.setOnClickListener {
                openGallery()
            }
            btnSave.setOnClickListener {
                val name = etUsername.text.toString()
                val career = etCareer.text.toString()
                val address = etAddress.text.toString()
                contact = Contact(contact.id, viewModel.photo.value, name, career, address)

                viewModel.addContact(contact)
                dismiss()
            }
            ibBack.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photo.collect { photo ->
                    binding.ivAddContactAvatar.loadImageWithGlide(photo)
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        photoActivityResult.launch(intent)
    }

}