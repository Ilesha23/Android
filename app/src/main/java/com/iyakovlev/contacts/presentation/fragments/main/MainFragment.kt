package com.iyakovlev.contacts.presentation.fragments.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentMainBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObservers()

    }

    private fun setListeners() {
        with(binding) {
            btnViewContacts.setOnClickListener {
                navController.navigate(MainFragmentDirections.actionMainFragmentToContactsFragment())
            }
            btnLogout?.setOnClickListener {
                viewModel.deleteUserData()
                navController.navigate(MainFragmentDirections.actionMainFragmentToSignInFragment())
                log("nav", true)
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
                        }

                        is Resource.Loading -> {
                            // TODO: progerssbar
                        }

                        is Resource.Success -> {
                            setData()
                            log(resource.data?.accessToken.toString(), true)
                        }
                    }
                }

            }
        }
    }

    private fun setData() {
        val user = viewModel.state.value.data
        binding.tvFullName.text = user?.name ?: getString(R.string.default_name)
        binding.tvCareer.text = user?.career ?: getString(R.string.career_placeholder)
        binding.tvAddress.text = user?.address ?: getString(R.string.address)
    }

}