package com.iyakovlev.contacts.presentation.fragments.sign_in

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.databinding.FragmentSignInBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment

class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            Toast.makeText(context, "asdasd", Toast.LENGTH_LONG).show()
        }
        binding.btnSignIn.setOnClickListener {
            navController.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

}