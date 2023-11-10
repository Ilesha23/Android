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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val args: ContactDetailViewFragmentArgs by navArgs()
        binding.ivAvatar.loadImageWithGlide(args.contactPhoto)
        binding.tvFullName.text = args.contactName
        binding.tvCareer.text = args.contactCareer
        binding.tvAddress.text = args.contactAddress
    }


    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }
    }

}