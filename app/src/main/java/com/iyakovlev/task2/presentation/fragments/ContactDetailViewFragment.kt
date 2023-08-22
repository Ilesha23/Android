package com.iyakovlev.task2.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.FragmentContactDetailViewBinding
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.presentation.common.BaseFragment
import com.iyakovlev.task2.utils.TestingConstants.isUsingTransactions
import com.iyakovlev.task2.utils.loadImageWithGlide

class ContactDetailViewFragment :
    BaseFragment<FragmentContactDetailViewBinding>(FragmentContactDetailViewBinding::inflate) {

    private lateinit var id: String
    private lateinit var photo: String
    private lateinit var name: String
    private lateinit var career: String
    private lateinit var address: String

    private val args: ContactDetailViewFragmentArgs by navArgs()


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
//        sharedElementEnterTransition = animation
//        sharedElementReturnTransition = animation
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isUsingTransactions) {
            arguments?.let { // TODO: change to navArgs
                id = it.getString("contactId", "")
                photo = it.getString("contactPhoto", "")
                name = it.getString("contactName", "")
                career = it.getString("contactCareer", "")
                address = it.getString("contactAddress", "")
            }
        } else {
            val args: ContactDetailViewFragmentArgs by navArgs()
            id = args.contactId
            photo = args.contactPhoto
            name = args.contactName
            career = args.contactCareer
            address = args.contactAddress
        }

        binding.ivAvatar.transitionName = "contactImageTransition_list_$id"
        ViewCompat.setTransitionName(binding.ivAvatar, "contactImageTransition_detail_$id") //todo: ???

        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )

        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation



        setData()

        setListeners()
//        setObservers()
    }

    private fun setData() {
        binding.ivAvatar.loadImageWithGlide(photo)
        binding.tvFullName.text = name
        binding.tvCareer.text = career
        binding.tvAddress.text = address
    }

    override fun setObservers() {
        TODO("Not yet implemented")
    }

    override fun setListeners() {
        binding.btnBack.setOnClickListener {
            if (isUsingTransactions) {
                parentFragmentManager.popBackStack()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(contact: Contact) =
            ContactDetailViewFragment().apply {

            }
    }
}