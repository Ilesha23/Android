package com.iyakovlev.contacts.presentation.fragments.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.model.UserEditRequest
import com.iyakovlev.contacts.data.repository.user.UserRepositoryImpl
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.use_case.EditUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val editUserUseCase: EditUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading())
    val state = _state.asStateFlow()

    val user = userRepositoryImpl.user()

    fun editUser(name: String, career: String?, phone: String?, address: String?, date: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val d = SimpleDateFormat("dd-mm-yyy", Locale.getDefault())
            d.parse(date)
            _state.emit(
                editUserUseCase(
                    UserEditRequest(
                        name = name,
                        career = career,
                        phone = phone,
                        address = address,
                        birthday = date,
                    )
                )
            )
        }
    }


}