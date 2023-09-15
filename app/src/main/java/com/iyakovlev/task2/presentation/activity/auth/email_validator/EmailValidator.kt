package com.iyakovlev.task2.presentation.activity.auth.email_validator

import android.util.Patterns

class EmailValidator(private val errorText: String, private val email: String) {

    fun validate(): String {
        return if ((email.isNotEmpty()) and (Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            ""
        } else {
            errorText
        }
    }

}