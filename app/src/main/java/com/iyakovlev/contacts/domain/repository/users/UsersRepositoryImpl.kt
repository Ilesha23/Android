package com.iyakovlev.contacts.domain.repository.users

import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.repository.contacts.ContactsRepository
import com.iyakovlev.contacts.domain.repository.user.UserRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//class UsersRepositoryImpl(private val apiService: ApiService) : ContactsRepository {
//
//    private val _state = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
//    override val state = _state.asStateFlow()
//
//    override suspend fun fetch(): Resource<List<UserRemote>> {
//        return try {
//            val response =
//                apiService.users(Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken.toString())
//            if (response.isSuccessful) {
//                if (response.body()?.data != null) {
//                    val c = response.body()!!.data.users.map {
//                        it.toUserRemote()
//                    }
////                    _state.emit(Resource.Success(response.body()?.contacts!!))
////                    Resource.Success(response.body()?.contacts!!)
//                    Resource.Success(c)
//                } else {
////                    _state.emit(Resource.Error("failed to get contacts list"))
//                    Resource.Error("no users found")
//                }
//            } else {
////                _state.emit(Resource.Error("failed to get contacts list"))
//                Resource.Error("failed to get users list")
//            }
//        } catch (e: Exception) {
////            _state.emit(Resource.Error("An error occurred getting contacts list"))
//            Resource.Error("An error occurred getting users list")
//        }
//    }
//
//}