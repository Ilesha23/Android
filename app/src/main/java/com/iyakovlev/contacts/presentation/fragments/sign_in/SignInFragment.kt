package com.iyakovlev.contacts.presentation.fragments.sign_in

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.common.constants.Constants.EMAIL
import com.iyakovlev.contacts.common.constants.Constants.PASS
import com.iyakovlev.contacts.common.constants.Constants.PREFERENCES
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentSignInBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
//import com.iyakovlev.contacts.presentation.utils.extensions.DataStore.get
//import com.iyakovlev.contacts.presentation.utils.extensions.DataStore.put
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel: SignInViewModel by viewModels()
//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObservers()

    }

    private fun setListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString() // TODO: check
                val pass = etPassword.text.toString()
                if (chkRemember.isChecked) {
                    viewModel.saveLogin(email, pass)
                }
                viewModel.login(email, pass)
            }
            btnSignUp.setOnClickListener {
                navController.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                launch {
                    viewModel.state.collect { resource ->
                        when (resource) {
                            is Resource.Error -> {
                                Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                            }

                            is Resource.Loading -> {
                                // TODO: progerssbar
                            }

                            is Resource.Success -> {
                                navController.navigate(SignInFragmentDirections.actionSignInFragmentToMainFragment())
                            }
                        }
                    }
//                }
//                launch {
//                    val email = get(requireContext(), EMAIL)
//                    val pass = get(requireContext(), PASS)
//                    if (!email.isNullOrBlank() and !pass.isNullOrBlank()) {
//                        viewModel.login(email.toString(), pass.toString())
//                    }
//                    viewModel.autologin()
//                }
            }

        }
    }

}