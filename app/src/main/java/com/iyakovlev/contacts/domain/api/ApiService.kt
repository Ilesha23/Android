package com.iyakovlev.contacts.domain.api

import com.iyakovlev.contacts.domain.api.model.LoginRequest
import com.iyakovlev.contacts.domain.api.model.RegisterRequest
import com.iyakovlev.contacts.domain.api.model.RegisterResponse
import com.iyakovlev.contacts.domain.api.model.UserContactsResponse
import com.iyakovlev.contacts.domain.api.model.UserEditRequest
import com.iyakovlev.contacts.domain.api.model.UserGetResponse
import com.iyakovlev.contacts.domain.api.model.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
    ): Response<RegisterResponse>

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

    @FormUrlEncoded
    @PUT("users/{userId}/contacts")
    suspend fun addContact(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Field("contactId") contactId: Long
    ): Response<UserContactsResponse>

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("contactId") contactId: Long
    ): Response<UserContactsResponse>

    @GET("users")
    suspend fun users(
        @Header("Authorization") token: String
    ): Response<UsersResponse>

}