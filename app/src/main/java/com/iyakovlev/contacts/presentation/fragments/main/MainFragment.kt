package com.iyakovlev.contacts.presentation.fragments.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.constants.Constants.EMAIL
import com.iyakovlev.contacts.common.constants.Constants.ISLOGINED
import com.iyakovlev.contacts.common.constants.Constants.NAME
import com.iyakovlev.contacts.common.constants.Constants.PASS
import com.iyakovlev.contacts.common.constants.Constants.PREFERENCES
import com.iyakovlev.contacts.common.constants.Constants.SURNAME
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentMainBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.fragments.sign_up_ext.SignUpExtFragmentDirections
import com.iyakovlev.contacts.presentation.utils.extensions.DataStore.delete
import com.iyakovlev.contacts.presentation.utils.extensions.capitalizeFirstChar
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
//    private lateinit var preferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels()
//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        preferences = requireContext().getSharedPreferences(
//            PREFERENCES,
//            AppCompatActivity.MODE_PRIVATE
//        )
//        parseEmailToPrefs()
//        setNameInTextView()

        setListeners()
        setObsevers()


    }

    private fun setListeners() {
        with(binding) {
            btnViewContacts.setOnClickListener {
//            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
//            viewPager.currentItem = 1
            }
            btnLogout?.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    delete(requireContext(), EMAIL)
                    delete(requireContext(), PASS)
                }
                navController.navigate(MainFragmentDirections.actionMainFragmentToSignInFragment())
//            preferences.edit().putString(EMAIL, null).apply()
//            preferences.edit().putBoolean(ISLOGINED, false).apply()
//            navigateToAuthActivity()
            }
        }
    }

    private fun setObsevers() {
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
                            binding.tvFullName.text = resource.data?.name ?: getString(R.string.default_name)
                            binding.tvCareer.text = resource.data?.career ?: getString(R.string.career_placeholder)
                            binding.tvAddress.text = resource.data?.address ?: getString(R.string.address)
                            log(resource.data?.accessToken.toString(), true)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToAuthActivity() {
//        val intent = Intent (activity, AuthActivity::class.java)
//        activity?.startActivity(intent)
//        parentFragmentManager.popBackStack()
//        activity?.finish()
    }

//    private fun parseEmailToPrefs() {
//        val email = preferences.getString(EMAIL, null)
//        if (email != null) {
//            val leftPart = email.split(DELIMITER_AT)[0]
//            if (leftPart.contains(DELIMITER_DOT)) {
//                val name = leftPart.split(DELIMITER_DOT)[0].capitalizeFirstChar()
//                val surname = leftPart.split(DELIMITER_DOT)[1].capitalizeFirstChar()
//                preferences.edit()
//                    .putString(NAME, name)
//                    .putString(SURNAME, surname)
//                    .apply()
//            }
//
//        } else {
//            preferences.edit()
//                .putString(NAME, getString(R.string.default_name))
//                .putString(SURNAME, getString(R.string.default_lastname))
//                .apply()
//        }
//    }

    private fun setNameInTextView() {
//        val name = preferences.getString(NAME, getString(R.string.default_name))
//        val lastname =
//            preferences.getString(SURNAME, getString(R.string.default_lastname))
//        val fullName = "$name $lastname"
//        binding.tvFullName.text = fullName

        binding.tvFullName.text = viewModel.user.value.name
    }

    companion object {

        const val DELIMITER_AT = "@"
        const val DELIMITER_DOT = "."

    }

}