package com.iyakovlev.contacts.presentation.fragments.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.repository.user.UserRepositoryImpl
import com.iyakovlev.contacts.domain.use_case.RegisterUserUseCase
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {

//    private val _userRegistration = MutableSharedFlow<Resource<User>>()
//    val userRegistration = _userRegistration.asSharedFlow()

//    private val _state = MutableStateFlow<UserRegisterState>(UserRegisterState.Init)
    private val _state = MutableSharedFlow<Resource<User>>()
    val state = _state.asSharedFlow()

    // TODO: savedstatehandle
    fun registerUser(email: String, pass: String) {
//        _state.emit(UserRegisterState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
//            registerUserUseCase(RegisterRequest(email, pass)).onEach { result ->
//                when(result) {
//                    is Resource.Success -> {
////                        _state.emit(UserRegisterState(user = result.data))
//                        _state.emit(UserRegisterState.Success(result.data))
//                    }
//                    is Resource.Error -> {
////                        _state.emit(UserRegisterState(error = "unexpected error"))
//                        _state.emit(UserRegisterState.Error("unexpected error"))
//                    }
//                    is Resource.Loading -> {
////                        _state.emit(UserRegisterState(true))
//                        _state.emit(UserRegisterState.Loading)
//                    }
//                }
//            }



            _state.emit(Resource.Loading())
//            _state.emit(Resource.Success(registerUserUseCase(RegisterRequest(email, pass))))
            _state.emit(Resource.Success(registerUserUseCase(RegisterRequest(email, pass))))
            log(_state.toString())


//            _state.emit(Resource.Success(UserRegisterState.Success(registerUserUseCase(RegisterRequest(email, pass)))))
//            _state.value = registerUserUseCase(RegisterRequest(email, pass))
        }
    }

}