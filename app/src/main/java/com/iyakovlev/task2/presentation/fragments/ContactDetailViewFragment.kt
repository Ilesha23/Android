package com.iyakovlev.task2.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.iyakovlev.task2.databinding.FragmentContactDetailViewBinding
import com.iyakovlev.task2.domain.Contact
import com.iyakovlev.task2.presentation.common.BaseFragment
import com.iyakovlev.task2.utils.TestingConstants.isUsingTransactions

class ContactDetailViewFragment : BaseFragment<FragmentContactDetailViewBinding>(FragmentContactDetailViewBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
//        setObservers()
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