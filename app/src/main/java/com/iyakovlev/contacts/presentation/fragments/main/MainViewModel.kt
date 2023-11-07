package com.iyakovlev.contacts.presentation.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.datastore.DataStore
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.repository.user.UserRepository
import com.iyakovlev.contacts.domain.use_case.GetUserUseCase
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val dataStore: DataStore,
//    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        log("init main view model", ISDEBUG)
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(getUserUseCase())
        }
    }

    fun deleteUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.delete(Constants.EMAIL)
            dataStore.delete(Constants.PASS)
        }
    }

//    fun getData(): User {
//        return userRepositoryImpl.getData()
//    }

}