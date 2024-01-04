package com.iyakovlev.contacts.data.repository.contacts

import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.api.ApiService
import com.iyakovlev.contacts.data.database.repository.DatabaseRepository
import com.iyakovlev.contacts.data.repository.user.UserRepositoryImpl
import com.iyakovlev.contacts.domain.model.UserRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val db: DatabaseRepository
) : ContactsRepository {

    private val _state = MutableStateFlow<Resource<List<UserRemote>>>(Resource.Loading())
    override val state = _state.asStateFlow()

    private suspend fun <T, E> performRequest(
        apiCall: suspend () -> Response<T>,
        onSuccess: (T) -> E,
        dbAction: suspend (T) -> Unit,
        onError: Int,
        onNoConnection: suspend () -> E
    ): Resource<E> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    val result = onSuccess(it)
                    dbAction(it)
                    Resource.Success(result)

                } ?: Resource.Error(onError)
            } else {
                Resource.Error(onError)
            }
        } catch (e: Exception) {
            Resource.Success(onNoConnection.invoke())
//            Resource.Error(R.string.error)
        }
    }

    override suspend fun getUsers(): Resource<List<UserRemote>> {
        return performRequest(
            apiCall = { apiService.users(Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken) },
            onSuccess = {
                it.data.users.map { it.toUserRemote() }.filter { it.name?.isNotBlank() == true }
            },
            dbAction = { }, // TODO:
            onError = R.string.error_users,
            onNoConnection = { listOf() } // TODO:
        )
    }

    override suspend fun fetch(): Resource<List<UserRemote>> {
        return performRequest(
            apiCall = {
                apiService.contacts(
                    Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken.toString(),
                    UserRepositoryImpl.user.id
                )
            },
            onSuccess = { it.data.contacts.map { it.toUserRemote() } },
            dbAction = {
                val l = it.data.contacts.map {
                    it.toContactEntity()
                }
                db.insertContactList(l)
            },
            onError = R.string.error_contacts,
            onNoConnection = { db.getContactList().map { it.toUserRemote() } }
        )
    }

    override suspend fun addContact(contactId: Long): Resource<List<UserRemote>> {
        return performRequest(
            apiCall = {
                apiService.addContact(
                    Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken,
                    UserRepositoryImpl.user.id,
                    contactId
                )
            },
            onSuccess = { it.data.contacts.map { it.toUserRemote() } },
            dbAction = {},
            onError = R.string.error_contact_add,
            onNoConnection = { listOf() } // TODO:  
        )
    }

    override suspend fun deleteContact(contactId: Long): Resource<List<UserRemote>> {
        return performRequest(
            apiCall = {
                apiService.deleteContact(
                    Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken,
                    UserRepositoryImpl.user.id,
                    contactId
                )
            },
            onSuccess = { it.data.contacts.map { it.toUserRemote() } },
            dbAction = {},
            onError = R.string.error_contact_delete,
            onNoConnection = { listOf() } // TODO:  
        )
    }

}