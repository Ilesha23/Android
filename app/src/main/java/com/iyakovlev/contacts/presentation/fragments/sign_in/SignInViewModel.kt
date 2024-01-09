package com.iyakovlev.contacts.presentation.fragments.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.domain.api.model.LoginRequest
import com.iyakovlev.contacts.data.repository.user.UserRepository
import com.iyakovlev.contacts.data.model.User
import com.iyakovlev.contacts.domain.use_case.AuthUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase,
    private val userRepository: UserRepository
) :
    ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authUserUseCase(LoginRequest(email, pass)).collect {
                _state.value = it
            }
        }
    }

    fun saveLogin(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUserLogin(email, pass)
        }
    }

}