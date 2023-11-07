package com.iyakovlev.contacts.presentation.fragments.sign_up.email_validator

import android.util.Patterns

class EmailValidator(private val errorText: String, private val email: String) {

    fun validate(): String {
        return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ""
        } else {
            errorText
        }
    }

}