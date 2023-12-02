package com.iyakovlev.contacts.domain.repository.contacts

import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.domain.repository.user.UserRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
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
                    Resource.Error(R.string.error_users)
                }
            } else {
                Resource.Error(R.string.error_users)
            }
        } catch (e: Exception) {
            Resource.Error(R.string.error)
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
                    Resource.Success(c)
                } else {
                    Resource.Error(R.string.error_contacts)
                }
            } else {
                Resource.Error(R.string.error_contacts)
            }
        } catch (e: Exception) {
            Resource.Error(R.string.error)
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
                    Resource.Error(R.string.error_contact_add)
                }
            } else {
                Resource.Error(R.string.error_contact_add)
            }
        } catch (e: Exception) {
            Resource.Error(R.string.error)
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
                    Resource.Error(R.string.error_contact_delete)
                }
            } else {
                Resource.Error(R.string.error_contact_delete)
            }
        } catch (e: Exception) {
            Resource.Error(R.string.error)
        }
    }

}