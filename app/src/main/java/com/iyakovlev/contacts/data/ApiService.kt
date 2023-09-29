package com.iyakovlev.contacts.data

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>/*RegisterResponse*/

}