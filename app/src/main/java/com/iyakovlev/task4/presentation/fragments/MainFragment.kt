package com.iyakovlev.task4.presentation.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.iyakovlev.task4.R
import com.iyakovlev.task4.common.Constants
import com.iyakovlev.task4.databinding.FragmentMainBinding
import com.iyakovlev.task4.presentation.common.BaseFragment
import com.iyakovlev.task4.utils.Constants.LOG_TAG
import com.iyakovlev.task4.utils.extensions.capitalizeFirstChar

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e(LOG_TAG, "main fragment")

        preferences = requireContext().getSharedPreferences(
            Constants.APP_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        parseEmailToPrefs()
        setNameInTextView()

    }

    private fun parseEmailToPrefs() {
        val email = preferences.getString(Constants.EMAIL, null)
        if (email != null) {
            val leftPart = email.split(Constants.DELIMITER_AT)[0]
            if (leftPart.contains(Constants.DELIMITER_DOT)) {
                val name = leftPart.split(Constants.DELIMITER_DOT)[0].capitalizeFirstChar()
                val surname = leftPart.split(Constants.DELIMITER_DOT)[1].capitalizeFirstChar()
                preferences.edit()
                    .putString(Constants.NAME, name)
                    .putString(Constants.SURNAME, surname)
                    .apply()
            }

        } else {
            preferences.edit()
                .putString(Constants.NAME, getString(R.string.default_name))
                .putString(Constants.SURNAME, getString(R.string.default_lastname))
                .apply()
        }
    }

    private fun setNameInTextView() {
        val name = preferences.getString(Constants.NAME, getString(R.string.default_name))
        val lastname =
            preferences.getString(Constants.SURNAME, getString(R.string.default_lastname))
        val fullName = "$name $lastname"
        binding.tvFullName.text = fullName
    }

    override fun setObservers() {
        TODO("Not yet implemented")
    }

    override fun setListeners() {
        TODO("Not yet implemented")
    }

}