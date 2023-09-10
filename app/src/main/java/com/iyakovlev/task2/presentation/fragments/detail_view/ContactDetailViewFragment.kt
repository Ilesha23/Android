package com.iyakovlev.task2.presentation.fragments.detail_view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.iyakovlev.task2.R
import com.iyakovlev.task2.common.constants.Constants.CONTACT_ADDRESS
import com.iyakovlev.task2.common.constants.Constants.CONTACT_CAREER
import com.iyakovlev.task2.common.constants.Constants.CONTACT_ID
import com.iyakovlev.task2.common.constants.Constants.CONTACT_NAME
import com.iyakovlev.task2.common.constants.Constants.CONTACT_PHOTO
import com.iyakovlev.task2.common.constants.Constants.TRANSITION_NAME
import com.iyakovlev.task2.common.constants.TestingConstants.isUsingTransactions
import com.iyakovlev.task2.databinding.FragmentContactDetailViewBinding
import com.iyakovlev.task2.presentation.base.BaseFragment
import com.iyakovlev.task2.presentation.utils.extensions.loadImageWithGlide

class ContactDetailViewFragment :
    BaseFragment<FragmentContactDetailViewBinding>(FragmentContactDetailViewBinding::inflate) {

    private lateinit var id: String
    private lateinit var photo: String
    private lateinit var name: String
    private lateinit var career: String
    private lateinit var address: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isUsingTransactions) {
            arguments?.let {
                id = it.getString(CONTACT_ID, "")
                photo = it.getString(CONTACT_PHOTO, "")
                name = it.getString(CONTACT_NAME, "")
                career = it.getString(CONTACT_CAREER, "")
                address = it.getString(CONTACT_ADDRESS, "")
            }
        } else {
            val args: ContactDetailViewFragmentArgs by navArgs()
            id = args.contactId
            photo = args.contactPhoto
            name = args.contactName
            career = args.contactCareer
            address = args.contactAddress
        }

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


    override fun setListeners() {
        binding.btnBack.setOnClickListener {
            if (isUsingTransactions) {
                parentFragmentManager.popBackStack()
            } else {
                navController.navigateUp()
            }
        }
    }

}