package com.iyakovlev.contacts.domain.use_case

import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.data.RegisterRequest
import com.iyakovlev.contacts.domain.model.User
import com.iyakovlev.contacts.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val userRepository: UserRepository) {

//    suspend operator fun invoke(body: RegisterRequest)/*: Flow<Resource<User>>*/ = flow {
//        try {
//            emit(Resource.Loading())
//            val user = userRepository.createUser(body).data.toUser()
//            emit(Resource.Success(user))
//        } catch (e: HttpException) {
//            emit(Resource.Error(e.localizedMessage ?: "unexpected error"))
//        } catch (e: IOException) {
//            emit(Resource.Error("Couldn't reach server"))
//        }
//    }

//    suspend operator fun invoke(body: RegisterRequest): User {
//        return userRepository.createUser(body).data.toUser()
//    }

    suspend operator fun invoke(body: RegisterRequest): Resource<User> {
        return userRepository.createUser(body)
    }

}
