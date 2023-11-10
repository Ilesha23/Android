package com.iyakovlev.contacts.presentation.fragments.sign_up_ext

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentSignUpExtBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpExtFragment : BaseFragment<FragmentSignUpExtBinding>(FragmentSignUpExtBinding::inflate) {

    private val viewModel: SignUpExtendedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObservers()

    }

    private fun setListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                toggleLoading(true)
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                viewModel.editUser(name, phone)
            }
            btnCancel.setOnClickListener {
                navController.navigateUp()
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                            toggleLoading(false)
                        }
                        is Resource.Loading -> {
                            // progressbar
                        }
                        is Resource.Success -> {
                            toggleLoading(false)
                            binding.tvYourData.text = resource.data.toString()
                            binding.tvFillFormProposal.text = resource.data?.accessToken.toString()
                            log(resource.data?.accessToken.toString(), true)
                            navController.navigate(SignUpExtFragmentDirections.actionSignUpExtFragmentToMainFragment())
//                            navController.navigate(R.id.mainFragment)
                        }
                    }
                }
            }
        }
    }

    private fun toggleLoading (isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                btnRegister.text = ""
                pbRegister.visibility = View.VISIBLE
            } else {
                btnRegister.text = getString(R.string.btn_signup)
                pbRegister.visibility = View.GONE
            }
        }
    }

}