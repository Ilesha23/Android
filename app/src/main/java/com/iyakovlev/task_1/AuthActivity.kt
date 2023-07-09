package com.iyakovlev.task_1

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.iyakovlev.task_1.databinding.AuthLayoutBinding

//TODO rounded image
const val APP_PREFERENCES = "APP_PREFERENCES"
const val EMAIL = "EMAIL"
const val ISLOGINED = "ISLOGINED"

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthLayoutBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        autoLogin()

        super.onCreate(savedInstanceState)
        binding = AuthLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionListeners()

    }

    private fun setupActionListeners() {
        binding.btnRegister.setOnClickListener {
            if (isInputValid(binding.emailText, binding.passwordText)) {
                preferences.edit()
                    .putString(EMAIL, binding.emailText.text.toString())
                    .apply()
                if (binding.chkRemember.isChecked) {
                    preferences.edit()
                        .putBoolean(ISLOGINED, true)
                        .apply()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        }
    }

    private fun autoLogin() {
        if (preferences.getBoolean(ISLOGINED, false)) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun isEmailValid(email: String): Boolean {
        if (!email.matches(Regex("""[\w-\.]+@([\w-]+\.)+[\w-]{2,4}"""))) {
            return false
        }
        return true
    }

    private fun isPassValid(pass: String): Boolean {
        if (!pass.matches(Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}${'$'}"""))) {
            return false
        }
        return true
    }

    private fun isInputValid(emailText: EditText, passwordText: EditText): Boolean {
        var isValid = true
        val email = emailText.text.toString()
        val pass = passwordText.text.toString()

        if (!isEmailValid(email)) {
            emailText.error = "Email is invalid"
            isValid = false
        }
        if (!isPassValid(pass)) {
            passwordText.error = "min 8 length, a-z, A-Z, 0-9 required"
            isValid = false
        }

        return isValid
    }
}