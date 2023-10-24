package com.iyakovlev.contacts.presentation.fragments.splashscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentSplashScreenBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenFragment :
    BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        viewModel.checkLogin()

    }

    // TODO: rewrite usecases to flows
    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { resource ->
                    when(resource) {
                        is Resource.Loading -> {

                        }
                        is Resource.Error -> {
                            log("error", true)
//                            navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToSignInFragment())
                        }
                        is Resource.Success -> {
//                            navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment())
                        }
                    }
                }
            }
        }
    }

    }