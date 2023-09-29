package com.iyakovlev.contacts.domain.repository.user

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.ApiService
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.data.RegisterResponse
import com.iyakovlev.contacts.data.toUser
import com.iyakovlev.contacts.domain.model.User
import java.lang.Exception
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val apiService: ApiService) : UserRepository {

    //    override suspend fun createUser(body: RegisterRequest): Resource<User> {
//        return try {
//            val response = apiService.register(body)//.data.data.toUser()
//            if (response.data?.code in 200..299) {
//                Resource.Success(response.data!!.data.toUser())
//            } else {
//                Resource.Error("error", null)
//            }
//        } catch (e: Exception) {
//            Resource.Error("error")
//        }
//    }
    override suspend fun createUser(body: RegisterRequest): RegisterResponse {
        return apiService.register(body)
    }

}