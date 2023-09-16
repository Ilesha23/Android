package com.iyakovlev.contacts.presentation.fragments.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants.EMAIL
import com.iyakovlev.contacts.common.constants.Constants.ISLOGINED
import com.iyakovlev.contacts.common.constants.Constants.NAME
import com.iyakovlev.contacts.common.constants.Constants.PREFERENCES
import com.iyakovlev.contacts.common.constants.Constants.SURNAME
import com.iyakovlev.contacts.databinding.FragmentMainBinding
import com.iyakovlev.contacts.presentation.activity.auth.AuthActivity
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.utils.extensions.capitalizeFirstChar

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private lateinit var preferences: SharedPreferences

//    private var name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireContext().getSharedPreferences(
            PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        parseEmailToPrefs()
        setNameInTextView()

        setListeners()

    }

    private fun setListeners() {
        binding.btnViewContacts.setOnClickListener {
            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
            viewPager.currentItem = 1
        }
        binding.btnLogout?.setOnClickListener {
            preferences.edit().putString(EMAIL, null).apply()
            preferences.edit().putBoolean(ISLOGINED, false).apply()
            navigateToAuthActivity()
        }
    }

    private fun navigateToAuthActivity() {
        val intent = Intent (activity, AuthActivity::class.java)
        activity?.startActivity(intent)
        parentFragmentManager.popBackStack()
        activity?.finish()
    }

    private fun parseEmailToPrefs() {
        val email = preferences.getString(EMAIL, null)
        if (email != null) {
            val leftPart = email.split(DELIMITER_AT)[0]
            if (leftPart.contains(DELIMITER_DOT)) {
                val name = leftPart.split(DELIMITER_DOT)[0].capitalizeFirstChar()
                val surname = leftPart.split(DELIMITER_DOT)[1].capitalizeFirstChar()
                preferences.edit()
                    .putString(NAME, name)
                    .putString(SURNAME, surname)
                    .apply()
            }

        } else {
            preferences.edit()
                .putString(NAME, getString(R.string.default_name))
                .putString(SURNAME, getString(R.string.default_lastname))
                .apply()
        }
    }

    private fun setNameInTextView() {
        val name = preferences.getString(NAME, getString(R.string.default_name))
        val lastname =
            preferences.getString(SURNAME, getString(R.string.default_lastname))
        val fullName = "$name $lastname"
        binding.tvFullName.text = fullName
    }

    companion object {

        const val DELIMITER_AT = "@"
        const val DELIMITER_DOT = "."

    }

}