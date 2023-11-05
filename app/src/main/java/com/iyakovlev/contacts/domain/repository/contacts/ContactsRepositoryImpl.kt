package com.iyakovlev.contacts.domain.repository.contacts

import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.repository.user.UserRepositoryImpl
import com.iyakovlev.contacts.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsRepositoryImpl(private val apiService: ApiService) : ContactsRepository {

    private val _state = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
    override val state = _state.asStateFlow()

    override suspend fun getUsers(): Resource<List<UserRemote>> {
        return try {
            val response =
                apiService.users(Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken.toString())
            if (response.isSuccessful) {
                if (response.body()?.data != null) {
                    val c = response.body()!!.data.users.map {
                        it.toUserRemote()
                    }
                    Resource.Success(c)
                } else {
                    Resource.Error("no users found")
                }
            } else {
                Resource.Error("failed to get users list")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred getting users list")
        }
    }

    override suspend fun fetch(): Resource<List<UserRemote>> {
        return try {
            val response =
                apiService.contacts(Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken.toString(), UserRepositoryImpl.user.id)
            if (response.isSuccessful) {
                if (response.body()?.data != null) {
                    val c = response.body()!!.data.contacts.map {
                        it.toUserRemote()
                    }
//                    _state.emit(Resource.Success(response.body()?.contacts!!))
//                    Resource.Success(response.body()?.contacts!!)
                    Resource.Success(c)
                } else {
//                    _state.emit(Resource.Error("failed to get contacts list"))
                    Resource.Error("no contacts found")
                }
            } else {
//                _state.emit(Resource.Error("failed to get contacts list"))
                Resource.Error("failed to get contacts list")
            }
        } catch (e: Exception) {
//            _state.emit(Resource.Error("An error occurred getting contacts list"))
            Resource.Error("An error occurred getting contacts list")
        }
    }

    override suspend fun addContact(contactId: Long): Resource<List<UserRemote>> {
        return try {
            val response =
                apiService.addContact(Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken.toString(), UserRepositoryImpl.user.id, contactId)
//            val users =
            if (response.isSuccessful) {
                if (response.body()?.data != null) {
                    val c = response.body()!!.data.contacts.map {
                        it.toUserRemote()
                    }
                    Resource.Success(c)
                } else {
                    Resource.Error("no contacts found")
                }
            } else {
                Resource.Error("failed to get contacts list")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred getting contacts list")
        }
    }

    override suspend fun deleteContact(contactId: Long): Resource<List<UserRemote>> {
        return try {
            val response =
                apiService.deleteContact(Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken.toString(), UserRepositoryImpl.user.id, contactId)
            if (response.isSuccessful) {
                if (response.body()?.data != null) {
                    val c = response.body()!!.data.contacts.map {
                        it.toUserRemote()
                    }
                    Resource.Success(c)
                } else {
                    Resource.Error("no contacts found")
                }
            } else {
                Resource.Error("failed to get contacts list")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred getting contacts list")
        }
    }

}