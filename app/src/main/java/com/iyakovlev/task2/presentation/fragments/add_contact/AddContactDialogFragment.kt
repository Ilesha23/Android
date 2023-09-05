package com.iyakovlev.task2.presentation.fragments.add_contact

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.AddContactDialogBinding
import com.iyakovlev.task2.data.model.Contact
import com.iyakovlev.task2.presentation.fragments.contacts.ContactsViewModel
import com.iyakovlev.task2.presentation.utils.extensions.loadImageWithGlide
import com.iyakovlev.task2.utils.log
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class AddContactDialogFragment : AppCompatDialogFragment() {

    private var _binding: AddContactDialogBinding? = null
    private val binding get() = requireNotNull(_binding)
//    private var viewModel = ContactsViewModel()
//    private val viewModel: ContactsViewModel by activityViewModels()
    @Inject lateinit var viewModel: ContactsViewModel

    private lateinit var photoActivityResult: ActivityResultLauncher<Intent>
    private var contact = Contact(UUID.randomUUID(), "", "", "", "")

//    fun setViewModel(viewModel: ContactsViewModel) {
//        this.viewModel = viewModel
//    }

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
            print(lifecycle)
        }
        Log.e("TAG", viewModel.toString())
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

//                val args: AddContactDialogFragmentArgs by navArgs()
//                viewModel = args.viewModel
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