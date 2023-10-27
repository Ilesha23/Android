package com.iyakovlev.contacts.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("users")
    suspend fun register(
        @Body body: RegisterRequest
    ): Response<RegisterResponse>/*RegisterResponse*/

    @PUT("users/{userId}")
    @Headers("Content-type: application/json")
    suspend fun edit(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Body body: UserEditRequest
    ): Response<UserGetResponse>

    @GET("users/{userId}")
    suspend fun get(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long
    ): Response<UserGetResponse>

    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<RegisterResponse>

    @GET("users/{userId}/contacts")
    suspend fun contacts(
        @Header("Authorization") token: String,
        @Path("userId") id: Long
    ): Response<UserContactsResponse>

    @GET("users")
    suspend fun users(
        @Header("Authorization") token: String
    ): Response<UsersResponse>

}