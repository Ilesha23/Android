package com.iyakovlev.contacts.presentation.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.data.UserGetRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.repository.user.UserRepository
import com.iyakovlev.contacts.domain.use_case.GetUserUseCase
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._com_iyakovlev_contacts_di_UserRepositoryModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUserUseCase: GetUserUseCase) : ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = Resource.Loading()
            _state.value = getUserUseCase()
            if (_state.value.data == null) {
                log("ERROR", true)
            } else {
                _user.value = _state.value.data!!
            }
        }
    }

}