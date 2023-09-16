package com.iyakovlev.contacts.presentation.fragments.detail_view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants.TRANSITION_NAME
import com.iyakovlev.contacts.databinding.FragmentContactDetailViewBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.utils.extensions.loadImageWithGlide

class ContactDetailViewFragment :
    BaseFragment<FragmentContactDetailViewBinding>(FragmentContactDetailViewBinding::inflate) {

    private lateinit var id: String
    private lateinit var photo: String
    private lateinit var name: String
    private lateinit var career: String
    private lateinit var address: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ContactDetailViewFragmentArgs by navArgs()
        id = args.contactId
        photo = args.contactPhoto
        name = args.contactName
        career = args.contactCareer
        address = args.contactAddress

        binding.ivAvatar.transitionName = "$TRANSITION_NAME$id"
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            R.transition.contact_image_transition
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        setData()

        setListeners()
    }

    private fun setData() {
        binding.ivAvatar.loadImageWithGlide(photo)
        binding.tvFullName.text = name
        binding.tvCareer.text = career
        binding.tvAddress.text = address
    }


    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }
    }

}