package com.iyakovlev.task_1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.iyakovlev.task_1.databinding.MainLayoutBinding

class MainActivity : BaseActivity<MainLayoutBinding>(MainLayoutBinding::inflate) {

    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        parseEmailToPrefs()
        setNameInTextView()

    }

    private fun parseEmailToPrefs() {
        val email = preferences.getString(EMAIL, null)
        if (email != null) {
            val leftPart = email.split("@")[0]
            var name = "Name"
            var surname = "Surname"
            if (leftPart.contains(".")) {
                name = leftPart.split(".")[0].replaceFirstChar { it.uppercaseChar() }
                surname = leftPart.split(".")[1].replaceFirstChar { it.uppercaseChar() }
            }
            preferences.edit()
                .putString(NAME, name)
                .putString(SURNAME, surname)
                .apply()
        } else {
            preferences.edit()
                .putString(NAME, "name") // TODO: strings
                .putString(SURNAME, "surname") // TODO: strings
                .apply()
        }
    }

    private fun setNameInTextView() {
        val name = preferences.getString(NAME, getString(R.string.default_name))
        val lastname = preferences.getString(SURNAME, getString(R.string.default_lastname))
        binding.nameText.text = "$name $lastname" // TODO:
    }

}