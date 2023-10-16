package com.iyakovlev.contacts.presentation.fragments.sign_up_ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.data.UserEditRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.use_case.EditUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(private val editUserUseCase: EditUserUseCase) : ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    fun editUser(name: String, phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = Resource.Loading()
            _state.value = editUserUseCase(UserEditRequest(name = name, phone = phone))
        }
    }

}