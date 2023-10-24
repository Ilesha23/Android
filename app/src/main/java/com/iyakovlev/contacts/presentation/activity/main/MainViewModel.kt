package com.iyakovlev.contacts.presentation.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.LoginRequest
import com.iyakovlev.contacts.domain.datastore.DataStore
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.use_case.AuthUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase,
    private val dataStore: DataStore
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    private var email = ""
    private var pass = ""

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            email = dataStore.get(Constants.EMAIL).toString()
            pass = dataStore.get(Constants.PASS).toString()
            if (email.isNotBlank() and pass.isNotBlank()) {
                login(email, pass)
            } else {
                _state.value = Resource.Error("no login data")
            }
        }
    }

    private fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authUserUseCase(LoginRequest(email, pass)).collect {
                when (it) {
                    is Resource.Loading -> {
                        _state.value = Resource.Loading()
                    }

                    is Resource.Error -> {
                        _state.value = Resource.Error("error logging in")
                    }

                    is Resource.Success -> {
                        _state.value = Resource.Success(it.data!!)
                    }
                }
            }

//            _state.value = authUserUseCase(LoginRequest(email, pass))
//            when (_state.value) {
//                is Resource.Loading -> {
//                    _s
//                }
//                is Resource.Error -> {
//                    _isLoading.value = false
//                }
//                is Resource.Success -> {
//                    _isLoading.value = false
//                }
//            }
        }
    }


}