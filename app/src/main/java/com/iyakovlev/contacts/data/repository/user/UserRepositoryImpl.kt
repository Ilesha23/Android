package com.iyakovlev.contacts.data.repository.user

import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.constants.Constants.AUTHORISATION_HEADER
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.api.ApiService
import com.iyakovlev.contacts.data.database.entities.ProfileEntity
import com.iyakovlev.contacts.data.database.repository.DatabaseRepository
import com.iyakovlev.contacts.data.model.LoginRequest
import com.iyakovlev.contacts.data.model.RegisterRequest
import com.iyakovlev.contacts.data.model.UserEditRequest
import com.iyakovlev.contacts.domain.datastore.DataStore
import com.iyakovlev.contacts.domain.model.User
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStore,
    private val db: DatabaseRepository
) : UserRepository {

    private suspend fun <T> performRequest(
        apiCall: suspend () -> Response<T>,
        onSuccess: (T) -> User,
        onError: Int,
        isError: Boolean
    ): Resource<User> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    user = onSuccess(it)
                    db.insertProfile(ProfileEntity(user.id, user.name, user.phone, user.address, user.career, user.birthday, user.image))
                    Resource.Success(user)
                } ?: Resource.Error(onError)
            } else {
                Resource.Error(onError)
            }
        } catch (e: Exception) {
//            Resource.Error(R.string.error)
            // TODO: refactor
            if (isError) {
                Resource.Error(R.string.error)
            } else {
                val user = db.getProfile()
                Resource.Success(User(user.id, user.name, user.phone, user.address, user.career, user.birthday, user.image)) // TODO:
            }
        }
    }

    override suspend fun createUser(body: RegisterRequest): Resource<User> {
        return performRequest(
            apiCall = { apiService.register(body) },
            onSuccess = { it.data.user.toUser(it.data.accessToken, it.data.refreshToken) },
            onError = R.string.error_user_registration,
            isError = false
        )
    }

    override suspend fun editUser(body: UserEditRequest): Resource<User> {
        return performRequest(
            apiCall = {
                apiService.edit(
                    AUTHORISATION_HEADER + user.accessToken.toString(),
                    user.id,
                    body
                )
            },
            onSuccess = {
                it.data.user.toUser(
                    user.accessToken.toString(),
                    user.refreshToken.toString()
                )
            },
            onError = R.string.error_user_edit,
            isError = false
        )
    }

    override suspend fun getUser(): Resource<User> {
        return performRequest(
            apiCall = { apiService.get(AUTHORISATION_HEADER + user.accessToken, user.id) },
            onSuccess = {
                it.data.user.toUser(
                    user.accessToken.toString(),
                    user.refreshToken.toString()
                )
            },
            onError = R.string.error_user_get,
            isError = false
        )
    }

    override suspend fun loginUser(body: LoginRequest): Resource<User> {
        return performRequest(
            apiCall = { apiService.login(body) },
            onSuccess = { it.data.user.toUser(it.data.accessToken, it.data.refreshToken) },
            onError = R.string.error_user_login,
            isError = true
        )
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