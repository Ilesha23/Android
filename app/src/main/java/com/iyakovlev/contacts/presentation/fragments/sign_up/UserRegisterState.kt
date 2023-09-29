package com.iyakovlev.contacts.presentation.fragments.sign_up

import com.iyakovlev.contacts.data.UserDto
import com.iyakovlev.contacts.domain.model.User

//data class UserRegisterState(
//    val isLoading: Boolean = false,
//    val user: User? = null,
//    val error: String = ""
//)

sealed class UserRegisterState{
    object  Init : UserRegisterState()
    data class Success(val user: User?) : UserRegisterState()
    data class Error(val error: String) : UserRegisterState()
    object Loading: UserRegisterState()
}
