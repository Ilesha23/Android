package com.iyakovlev.task_1.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.iyakovlev.task_1.R
import com.iyakovlev.task_1.common.Constants.APP_PREFERENCES
import com.iyakovlev.task_1.common.Constants.EMAIL
import com.iyakovlev.task_1.common.Constants.ISLOGINED
import com.iyakovlev.task_1.common.Constants.PASSWORD_LENGTH
import com.iyakovlev.task_1.common.Constants.PASSWORD_LOWERCASE_LETTERS
import com.iyakovlev.task_1.common.Constants.PASSWORD_NUMBERS
import com.iyakovlev.task_1.common.Constants.PASSWORD_UPPERCASE_LETTERS
import com.iyakovlev.task_1.databinding.AuthLayoutBinding


class AuthActivity : BaseActivity<AuthLayoutBinding>(AuthLayoutBinding::inflate) {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        autoLogin()

        super.onCreate(savedInstanceState)

        setListeners()

    }

    /* disables focus from EditText if clicked outside of it */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    isInputValid(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun setListeners() {
        binding.btnRegister.setOnClickListener {
            onRegisterClick()
        }
        binding.btnGoogle?.setOnClickListener {
//            Toast.makeText(applicationContext, "click", Toast.LENGTH_LONG).show()
        }
    }

    /* registration button clicked */
    private fun onRegisterClick() {
        with(binding) {
            if (isInputValid(etEmail.text.toString(), etPassword.text.toString())) {

                with(preferences) {
                    edit()
                        .putString(EMAIL, etEmail.text.toString())
                        .apply()

                    if (chkRemember.isChecked) {
                        edit()
                            .putBoolean(ISLOGINED, true)
                            .apply()
                    }
                }

                goToMainActivity()

            }
        }
    }

    /* Starts main activity with animation */
    private fun goToMainActivity() {
        startActivity(
            Intent(this@AuthActivity, MainActivity::class.java),
            ActivityOptionsCompat
                .makeCustomAnimation(this, R.anim.slide_start, R.anim.slide_end)
                .toBundle()
        )
        finish()

//        or
//        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
//        overridePendingTransition(R.anim.slide_start, R.anim.slide_end)
//        finish()
    }

    /* Automatically pass register screen if user is logined */
    private fun autoLogin() {
        if (preferences.getBoolean(ISLOGINED, false)) {
            goToMainActivity()
        }
    }
    private fun isEmailValid(email: String): Boolean {
        return (email.isNotEmpty()) and (Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    private fun isPassValid(pass: String): Boolean {
        if (!pass.matches(Regex( /* at least 1 lowercase, 1 uppercase, 1 number, length 8+ */
                "^(?=.*[$PASSWORD_LOWERCASE_LETTERS])" +
                    "(?=.*[$PASSWORD_UPPERCASE_LETTERS])" +
                    "(?=.*[$PASSWORD_NUMBERS])" +
                    "[$PASSWORD_LOWERCASE_LETTERS$PASSWORD_UPPERCASE_LETTERS$PASSWORD_NUMBERS]" +
                    "{$PASSWORD_LENGTH,}$"
            ))) {
            return false
        }
        return true
    }

    private fun isInputValid(email: String, password: String): Boolean {
        var isValid = true

        // email
        if (!isEmailValid(email)) {
            binding.tilEmail.helperText = getString(R.string.error_email)
            isValid = false
        } else {
            binding.tilEmail.helperText = null
        }

        // password
        if (!isPassValid(password)) {
            binding.tilPassword.helperText = formPassErrorText(password)
            isValid = false
        } else {
            binding.tilPassword.helperText = null
        }

        return isValid
    }

    /* forms error text of each incorrect case in password independently */
    private fun formPassErrorText(password: String): String {
        var errorMessage = ""

        if (password.length < 8) {
            errorMessage += "length $PASSWORD_LENGTH"
        }
        if (!password.contains(Regex("[$PASSWORD_LOWERCASE_LETTERS]"))) {
            errorMessage = "${addComma(errorMessage)}$PASSWORD_LOWERCASE_LETTERS"
        }
        if (!password.contains(Regex("[$PASSWORD_UPPERCASE_LETTERS]"))) {
            errorMessage = "${addComma(errorMessage)}$PASSWORD_UPPERCASE_LETTERS"
        }
        if (!password.contains(Regex("[$PASSWORD_NUMBERS]"))) {
            errorMessage = "${addComma(errorMessage)}$PASSWORD_NUMBERS"
        }

        return "$errorMessage required"
    }

    /* adds comma to error message if there is some text before */
    private fun addComma(s: String): String {
        if (s.isNotBlank()) {
            return "$s, "
        }
        return s
    }
}