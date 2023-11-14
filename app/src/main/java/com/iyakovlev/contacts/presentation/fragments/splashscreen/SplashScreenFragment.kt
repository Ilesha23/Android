package com.iyakovlev.contacts.presentation.fragments.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentSplashScreenBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment :
    BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setObservers()

    }

    private fun setObservers() {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.state.collect {
                        log("view model state collected in splash", Constants.ISDEBUG)
                        if (it is Resource.Success) {
                            navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment())
                            log("navigated splash -> main fr")
                        } else if (it is Resource.Error) {
                            navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToSignInFragment())
                            log("navigated splash -> sign in fr")
                        }
                    }
                }
            }
        }
    }

}