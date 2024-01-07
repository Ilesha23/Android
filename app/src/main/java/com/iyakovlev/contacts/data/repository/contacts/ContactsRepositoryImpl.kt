package com.iyakovlev.contacts.data.repository.contacts

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.api.ApiService
import com.iyakovlev.contacts.data.database.repository.DatabaseRepository
import com.iyakovlev.contacts.data.repository.user.UserRepositoryImpl
import com.iyakovlev.contacts.domain.model.UserRemote
import com.iyakovlev.contacts.utils.AppStatus.isFirstLaunch
import com.iyakovlev.contacts.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
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
        onSuccess: suspend (T) -> E,
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
            onSuccess = { it.data.users.map { it.toUserRemote() }.filter { it.name?.isNotBlank() == true } },
            dbAction = { db.insertUsers(it.data.users.map { it.toUserEntity() }.filter { it.name?.isNotBlank() == true }) },
            onError = R.string.error_users,// TODO: onerror code
            onNoConnection = { db.getUsers().map { it.toUserRemote() } }
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
            onSuccess = {// TODO: maybe refactor somehow
                // makes server and db info the same:

                val a = db.getContacts()
                if (!isFirstLaunch) { // todo: maybe delete this hack
                    // TODO: temporary table with deleted contacts maybe
                    // add contacts that are in db but not on server
                    val l = db.getContacts().filter { contactEntity ->
                        !it.data.contacts.map { it.toContactEntity() }.contains(contactEntity)
                    }
                    log("difference: $l", ISDEBUG)
                    for (i in l) {
                        addContact(i.id)
                    }

                    // delete contacts that are deleted from db but not deleted from server
                    val ll = it.data.contacts.map { it.toUserRemote() }.filter { user ->
                        !db.getContacts().map { it.toUserRemote() }.contains(user)
                    }
                    for (i in ll) {
                        deleteContact(i.id)
                    }
                }

                // return list from server
                apiService.contacts(
                    Constants.AUTHORISATION_HEADER + UserRepositoryImpl.user.accessToken.toString(),
                    UserRepositoryImpl.user.id
                ).body()?.data?.contacts?.map { it.toUserRemote() } ?: listOf()
                        },
            dbAction = { db.insertContacts(it.data.contacts.map { it.toContactEntity() }) },
            onError = R.string.error_contacts,
            onNoConnection = { db.getContacts().map { it.toUserRemote() } }
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
            dbAction = { db.insert(it.data.contacts.first { it.id == contactId }.toContactEntity()) },
            onError = R.string.error_contact_add,
            onNoConnection = {
                db.insert(db.getUser(contactId))
                db.getContacts().map { it.toUserRemote() }
            }
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
            dbAction = { db.delete(contactId) },
            onError = R.string.error_contact_delete,
            onNoConnection = {
                db.delete(contactId)
                db.getContacts().map { it.toUserRemote() }
            }
        )
    }

}