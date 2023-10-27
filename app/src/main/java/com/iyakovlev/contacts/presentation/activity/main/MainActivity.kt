package com.iyakovlev.contacts.presentation.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.ActivityContactsBinding
import com.iyakovlev.contacts.presentation.base.BaseActivity
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private var isSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        installSplashScreen().apply {
//            setKeepOnScreenCondition { // TODO: maybe splash screen fragment
//                isSplash
//            }
//        }

        setNavController()
        setObservers()

    }

    private fun setObservers() {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.state.collect {
                        log("view model state collected in main", ISDEBUG)
                        if (it is Resource.Success) {
                            navController.navigate(R.id.action_signInFragment_to_mainFragment)
                            isSplash = false
                            log("navigated main act -> main fr")
                        } else if (it is Resource.Error) {
                            isSplash = false
                        }
                    }
                }
            }
        }
    }

    private fun setNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (navHostFragment is NavHostFragment) {
            navController = navHostFragment.navController
        }
    }

}