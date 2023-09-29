package com.iyakovlev.contacts.presentation.fragments.add_contact

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.domain.model.Contact
import com.iyakovlev.contacts.databinding.AddContactDialogBinding
import com.iyakovlev.contacts.presentation.utils.extensions.loadImageWithGlide
import com.iyakovlev.contacts.utils.log
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID


@AndroidEntryPoint
class AddContactDialogFragment : AppCompatDialogFragment() {

    private val viewModel: AddContactViewModel by viewModels()

    private var _binding: AddContactDialogBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var photoActivityResult: ActivityResultLauncher<Intent>
    private var tempImage: File? = null
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

        requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?.let { deleteFilesInDirectory(it) }

        setListeners()
        setObservers()

        photoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = it.data?.data
                if (selectedImageUri != null) {
                    val uri = startImageCrop(selectedImageUri)
                    handleCroppedImage(uri)
                }
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
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        photoActivityResult.launch(intent)
    }

    private fun handleCroppedImage(uri: Uri?) {
        if (uri != null) {
            viewModel.setPhoto(uri.toString())
        }
    }

    private fun startImageCrop(sourceUri: Uri): Uri {
        val destinationUri = Uri.fromFile(createTempFile())

        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(requireContext(), this, UCrop.REQUEST_CROP)

        return destinationUri
    }

    private fun createTempFile(): File? {
        val imageFileName = "temp_image"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        tempImage = File.createTempFile(imageFileName, ".jpg", storageDir)

        return tempImage
    }

    fun deleteFilesInDirectory(directory: File) {
        if (directory.isDirectory) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        file.delete()
                    }
                }
            }
        }
    }

}