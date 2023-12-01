package com.iyakovlev.contacts.presentation.fragments.sign_up

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.repository.user.UserRepository
import com.iyakovlev.contacts.domain.use_case.RegisterUserUseCase
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val userRepository: UserRepository
) :
    ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    // TODO: savedstatehandle

    init {
        log("sign up ext viewmodel init", ISDEBUG)
    }

    fun clear() {
        _state.value = Resource.Loading()
    }

    fun registerUser(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = registerUserUseCase(RegisterRequest(email, pass))
        }
    }

    fun isLoginDataValid(email: String, pass: String): Boolean {
        if ((pass.length <= 16) and Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        return false
    }

    fun saveLogin(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUserLogin(email, pass)
        }
    }

}