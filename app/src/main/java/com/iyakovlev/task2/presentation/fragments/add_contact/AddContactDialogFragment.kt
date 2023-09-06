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
import com.iyakovlev.task2.R
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.data.repositories.contact.ContactRepository
import com.iyakovlev.task2.databinding.AddContactDialogBinding
import com.iyakovlev.task2.presentation.utils.extensions.loadImageWithGlide
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class AddContactDialogFragment : AppCompatDialogFragment() {

    private var _binding: AddContactDialogBinding? = null
    private val binding get() = requireNotNull(_binding)

    @Inject
    lateinit var repository: ContactRepository

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

    // TODO: onCreateDialog
//    @SuppressLint("InflateParams")
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(activity)
//        val inflater = requireActivity().layoutInflater
//        builder.setView(inflater.inflate(R.layout.add_contact_dialog, null))
//        return builder.create()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        photoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val photo = it.data?.data.toString()
                contact = Contact(contact.id, photo, contact.name, contact.career, contact.address)
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
                val address = etAddress.text.toString()
                contact = Contact(contact.id, contact.photo, name, career, address)

                repository.addContact(contact)
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