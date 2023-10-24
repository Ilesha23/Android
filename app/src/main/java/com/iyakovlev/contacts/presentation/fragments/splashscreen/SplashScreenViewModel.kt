package com.iyakovlev.contacts.presentation.fragments.splashscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.constants.Constants.EMAIL
import com.iyakovlev.contacts.common.constants.Constants.PASS
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
class SplashScreenViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase,
    dataStore: DataStore
) : ViewModel() {

    private val dataStore = dataStore

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

//    init {
//        checkLogin()
//    }

    fun checkLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            val email = dataStore.get(EMAIL)
            val pass = dataStore.get(PASS)
            login(email.toString(), pass.toString())
        }
    }

    private fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authUserUseCase(LoginRequest(email, pass)).collect {
                _state.value = it
            }
//            _state.value = Resource.Loading()
//            _state.value = authUserUseCase(LoginRequest(email, pass))
        }
    }



}