package com.iyakovlev.contacts.presentation.fragments.sign_up

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentSignUpBinding
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.presentation.activity.auth.email_validator.EmailValidator
import com.iyakovlev.contacts.presentation.activity.auth.password_validator.PasswordValidator
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setListeners()

    }

    private fun setListeners() {
        with(binding) {
            btnSignIn.setOnClickListener {
                navController.navigateUp()
            }
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString() // TODO: check
                val password = etPassword.text.toString()
                viewModel.registerUser(email, password)
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {resource ->
                    when (resource) {
                        is Resource.Error -> {
//                            binding.tvGreeting.text = resource.message
                            Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                        }
                        is Resource.Loading -> {
//                            binding.tvGreeting.text = "loading"
                            // TODO: progerssbar 

                        }
                        is Resource.Success -> {
                            log(resource.data?.accessToken.toString(), true)
                            navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpExtFragment())
                        }
                    }
                }

//                viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
//                    with(binding) {
//                        when(it) {
//                            is Resource.Error -> {
//                                binding.tvGreeting.text = "ewqqwe"
//                                log("error", true)
//                            }
//                            is Resource.Loading -> {
//                                binding.tvGreeting.text = "dsdasa"
//                                log("loading", true)
//                            }
//                            is Resource.Success -> {
//                                tvGreeting.text = it.data.toString()
//                                tvFillFormProposal.text = it.data?.accessToken
//                                log("${it.data?.accessToken}", true)
//                            }
//                        }
//                    }
//                }

//                launch {
//                    viewModel.state.collect {
//                        when(it) {
//
//                            is Resource.Loading -> {
//                                binding.tvGreeting.text = "loading"
//                            }
//                            is Resource.Error -> {
//                                binding.tvGreeting.text = "error"
//                            }
//                            is Resource.Success -> {
//                                binding.tvGreeting.text = it.data.toString()
//                            }
//                            /*is Resource.Loading<> -> {
//
//                            }*/
//
//
////                            is UserRegisterState.Loading -> {
////                                binding.tvGreeting.text = "loading"
////                            }
////                            is UserRegisterState.Error -> {
////                                binding.tvGreeting.text = "error"
////                            }
////                            is UserRegisterState.Success -> {
////                                binding.tvGreeting.text = it.user?.accessToken.toString()
////                                binding.tvFillFormProposal.text = it.user?.refreshToken.toString()
////                            }
////                            UserRegisterState.Init -> Unit
//                        }
//                    }
//                }
            }
        }
    }

//    private lateinit var preferences: SharedPreferences

//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
//        autoLogin()
//
//        super.onCreate(savedInstanceState)
//
//        setListeners()
//
//    }

//    /* disables focus from EditText if clicked outside of it */
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            val v = currentFocus
//            if (v is EditText) {
//                val outRect = Rect()
//                v.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//                    isInputValid(
//                        binding.etEmail.text.toString(),
//                        binding.etPassword.text.toString()
//                    )
//                    v.clearFocus()
//                    val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event)
//    }

//    fun hideKeyboard() {
//        hideKeyboard(currentFocus ?: View(this))
//    }
//
//    fun Context.hideKeyboard(view: View) {
//        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//    }

//    override fun setListeners() {
////        binding.root.setOnClickListener {
////            hideKeyboard()
////        }
//        binding.btnRegister.setOnClickListener {
//            onRegisterClick()
//        }
//        binding.btnGoogle.setOnClickListener {
////            Toast.makeText(applicationContext, "click", Toast.LENGTH_LONG).show()
//        }
//    }

    /* registration button clicked */
//    private fun onRegisterClick() {
//        with(binding) {
//            if (isInputValid(etEmail.text.toString(), etPassword.text.toString())) {
//
//                with(preferences) {
//                    edit()
//                        .putString(Constants.EMAIL, etEmail.text.toString())
//                        .apply()
//
//                    if (chkRemember.isChecked) {
//                        edit()
//                            .putBoolean(Constants.ISLOGINED, true)
//                            .apply()
//                    }
//                }
//
//                //goToMainActivity() // TODO: sign up extended
//
//            }
//        }
//    }


    /* Automatically pass register screen if user is logined */
    private fun autoLogin() {
//        if (preferences.getBoolean(Constants.ISLOGINED, false)) {
//            // TODO:
//        }
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