package com.iyakovlev.task2.presentation.fragments.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.iyakovlev.task2.R
import com.iyakovlev.task2.common.constants.Constants.EMAIL
import com.iyakovlev.task2.common.constants.Constants.NAME
import com.iyakovlev.task2.common.constants.Constants.PREFERENCES
import com.iyakovlev.task2.common.constants.Constants.SURNAME
import com.iyakovlev.task2.databinding.FragmentMainBinding
import com.iyakovlev.task2.presentation.base.BaseFragment
import com.iyakovlev.task2.presentation.utils.extensions.capitalizeFirstChar

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireContext().getSharedPreferences(
            PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        parseEmailToPrefs()
        setNameInTextView()

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

        const val DELIMITER_AT = "DELIMITER_AT"
        const val DELIMITER_DOT = "DELIMITER_DOT"

    }

}