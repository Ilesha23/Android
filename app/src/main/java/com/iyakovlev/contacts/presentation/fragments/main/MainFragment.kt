package com.iyakovlev.contacts.presentation.fragments.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.database.Database
import com.iyakovlev.contacts.databinding.FragmentMainBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var db: Database

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        log("main fr onviewcreated", ISDEBUG)
        setListeners()
        setObservers()
        viewModel.fetchUser()

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
            btnEditProfile.setOnClickListener {
                navController.navigate(MainFragmentDirections.actionMainFragmentToEditProfileFragment())
                log("navigated main -> edit profile")
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            Toast.makeText(context, getString(resource.message ?: R.string.error_user_get), Toast.LENGTH_LONG).show()
                            navController.navigate(MainFragmentDirections.actionMainFragmentToSignInFragment())
                        }
                        is Resource.Loading -> {
                            // progressbar
                        }
                        is Resource.Success -> {
                            binding.pbMain?.visibility = View.GONE
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
        binding.tvFullName.apply {
            text = user?.name ?: getString(R.string.default_name)
            visibility = View.VISIBLE
        }
        binding.tvCareer.apply {
            text = user?.career ?: getString(R.string.career_placeholder)
            visibility = View.VISIBLE
        }
        binding.tvAddress.apply {
            text = user?.address ?: getString(R.string.address)
            visibility = View.VISIBLE
        }
    }

}