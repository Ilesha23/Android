package com.iyakovlev.contacts.presentation.activity.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityOptionsCompat
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants.EMAIL
import com.iyakovlev.contacts.common.constants.Constants.ISLOGINED
import com.iyakovlev.contacts.common.constants.Constants.PREFERENCES
import com.iyakovlev.contacts.databinding.AuthLayoutBinding
import com.iyakovlev.contacts.presentation.activity.auth.email_validator.EmailValidator
import com.iyakovlev.contacts.presentation.activity.auth.password_validator.PasswordValidator
import com.iyakovlev.contacts.presentation.activity.main.MainActivity
import com.iyakovlev.contacts.presentation.base.BaseActivity


class AuthActivity : BaseActivity<AuthLayoutBinding>(AuthLayoutBinding::inflate) {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        preferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
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
                    isInputValid(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

//    fun hideKeyboard() {
//        hideKeyboard(currentFocus ?: View(this))
//    }
//
//    fun Context.hideKeyboard(view: View) {
//        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//    }

    override fun setListeners() {
//        binding.root.setOnClickListener {
//            hideKeyboard()
//        }
        binding.btnRegister.setOnClickListener {
            onRegisterClick()
        }
        binding.btnGoogle.setOnClickListener {
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
//        overridePendingTransition(R.anim.slide_start, R.anim.slide_end) // deprecated
//        finish()
    }

    /* Automatically pass register screen if user is logined */
    private fun autoLogin() {
        if (preferences.getBoolean(ISLOGINED, false)) {
            goToMainActivity()
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        var isValid = true

        // email
        val emailErrorText = EmailValidator(getString(R.string.error_email), email).validate()
        if (emailErrorText.isNotBlank()) {
            binding.tilEmail.helperText = emailErrorText
            isValid = false
        } else {
            binding.tilEmail.helperText = null
        }

        // password
        val passwordError = PasswordValidator(password).validate()
        if (passwordError.isNotBlank()) {
            binding.tilPassword.helperText = passwordError
            isValid = false
        } else {
            binding.tilPassword.helperText = null
        }

        return isValid
    }
}