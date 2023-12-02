package com.iyakovlev.contacts.domain.repository.user

import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.constants.Constants.AUTHORISATION_HEADER
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.data.LoginRequest
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.data.UserEditRequest
import com.iyakovlev.contacts.domain.datastore.DataStore
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.utils.log
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStore
) : UserRepository {

    override suspend fun createUser(body: RegisterRequest): Resource<User> {
        return try {
            val response = apiService.register(body)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    user = it.user.toUser(it.accessToken, it.refreshToken)
                    Resource.Success(user)
                } ?: Resource.Error(R.string.error_user_registration)
            } else {
                Resource.Error(R.string.error_user_registration)
            }
        } catch (e: Exception) {
            Resource.Error(R.string.error)
        }
    }

    // TODO: repeated
    override suspend fun editUser(body: UserEditRequest): Resource<User> {
        return try {
            val response =
                apiService.edit(AUTHORISATION_HEADER + user.accessToken.toString(), user.id, body)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    user = it.user.toUser(user.accessToken.toString(), user.refreshToken.toString())
                    Resource.Success(user)
                } ?: Resource.Error(R.string.error_user_edit)
            } else {
                Resource.Error(R.string.error_user_edit)
            }
        } catch (e: Exception) {
            Resource.Error(R.string.error)
        }
    }

    override suspend fun getUser(): Resource<User> {
        return try {
            val response =
                apiService.get(AUTHORISATION_HEADER + user.accessToken.toString(), user.id)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    user = it.user.toUser(user.accessToken.toString(), user.refreshToken.toString())
                    Resource.Success(user)
                } ?: Resource.Error(R.string.error_user_get)
            } else {
                Resource.Error(R.string.error_user_get)
            }
        } catch (e: Exception) {
            Resource.Error(R.string.error)
        }
    }

    override suspend fun loginUser(body: LoginRequest): Resource<User> {
        return try {
            val response = apiService.login(body)
            if (response.isSuccessful) {
                log(response.body()!!.data.toString(), ISDEBUG)
                response.body()?.data?.let {
                    user = it.user.toUser(it.accessToken, it.refreshToken)
                    Resource.Success(user)
                } ?: Resource.Error(R.string.error_user_login)
            } else {
                Resource.Error(R.string.error_user_login)
            }
        } catch (e: Exception) {
            log(e.message.toString(), ISDEBUG)
            Resource.Error(R.string.error)
        }
    }

    override suspend fun saveUserLogin(email: String, pass: String) {
        dataStore.put(Constants.EMAIL, email)
        dataStore.put(Constants.PASS, pass)
    }

    fun user() = user


    companion object {
        var user = User()
    }

}