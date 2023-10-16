package com.iyakovlev.contacts.presentation.fragments.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.use_case.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val registerUserUseCase: RegisterUserUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    // TODO: savedstatehandle
    fun registerUser(email: String, pass: String) {

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = Resource.Loading()
            _state.value = registerUserUseCase(RegisterRequest(email, pass))
        }
    }

}