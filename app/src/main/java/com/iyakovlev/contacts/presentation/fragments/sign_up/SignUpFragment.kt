package com.iyakovlev.contacts.presentation.fragments.sign_up

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentSignUpBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clear()
        setObservers()
        setListeners()

    }

    private fun setListeners() {
        with(binding) {
            btnSignIn.setOnClickListener {
                navController.navigateUp()
            }
            btnRegister.setOnClickListener {
                toggleLoading(true)
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                if (viewModel.isLoginDataValid(email, password)) {
                    viewModel.registerUser(email, password)
                    tilPassword.error = null
                } else {
                    tilPassword.error = "incorrect email or password" // ?
                    toggleLoading(false)
                }
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            toggleLoading(false)
                            Toast.makeText(context, getString(resource.message ?: R.string.error), Toast.LENGTH_LONG).show()
                        }

                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            log(resource.data?.accessToken.toString(), true)
                            toggleLoading(false)
                            if (binding.chkRemember.isChecked) {
                                viewModel.saveLogin(
                                    binding.etEmail.text.toString(),
                                    binding.etPassword.text.toString()
                                )
                            }
                            navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpExtFragment())
                        }
                    }
                }
            }
        }
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnRegister.text = ""
            binding.pbReg.visibility = View.VISIBLE
        } else {
            binding.btnRegister.text = getString(R.string.btn_signup)
            binding.pbReg.visibility = View.GONE
        }
    }

}