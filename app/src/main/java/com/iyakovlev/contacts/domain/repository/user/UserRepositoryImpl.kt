package com.iyakovlev.contacts.domain.repository.user

import com.iyakovlev.contacts.common.constants.Constants.AUTHORISATION_HEADER
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.data.LoginRequest
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.data.UserEditRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.utils.log
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val apiService: ApiService) : UserRepository {

    //private var user = User()

    override suspend fun createUser(body: RegisterRequest): Resource<User> {
        return try {
            val response = apiService.register(body)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    user = it.user.toUser(it.accessToken, it.refreshToken)
                    Resource.Success(user)
                } ?: Resource.Error("Registration failed")
            } else {
                Resource.Error("Registration failed or user exists")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred in create")
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
                } ?: Resource.Error("Edit failed")
            } else {
                Resource.Error("Edit failed")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred in edit")
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
                } ?: Resource.Error("Get failed")
            } else {
                Resource.Error("Get failed")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred in get")
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
                } ?: Resource.Error("Login failed")
            } else {
                Resource.Error("Login failed")
            }
        } catch (e: Exception) {
            log(e.message.toString(), ISDEBUG)
            Resource.Error("An error occurred in login")
        }
    }

    override fun getData(): User {
        return user
    }

    fun user() = user


    companion object {
        var user = User()
    }

}