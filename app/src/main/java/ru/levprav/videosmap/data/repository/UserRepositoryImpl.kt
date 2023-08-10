package ru.levprav.videosmap.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.levprav.videosmap.data.remote.UserApi
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            emit(Resource.Error("Please fill in all fields"))
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emit(Resource.Error("Incorrect Email format"))
        } else if (password != passwordConfirm) {
            emit(Resource.Error("Passwords don't match"))
        } else {
            val isUserExists = api.checkUserExists(email)
            if (isUserExists) {
                emit(Resource.Error("User with this Email already exists"))
            } else {
                try {
                    api.signUp(email, password)
                    emit(Resource.Success(Unit))
                } catch (e: Exception) {
                    emit(Resource.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun signIn(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        if (email.isEmpty() || password.isEmpty()) {
            emit(Resource.Error("Please fill in all fields"))
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emit(Resource.Error("Incorrect Email format"))
        } else {
            val isUserExists = api.checkUserExists(email)
            if (!isUserExists) {
                emit(Resource.Error("User with this Email doesn't exists"))
            } else {
                try {
                    api.signIn(email, password)
                    emit(Resource.Success(Unit))
                } catch (e: Exception) {
                    emit(Resource.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMyProfile(): Resource<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfileDetail(targetUid: String): Resource<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun saveProfile(profile: UserModel): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun follow(followedUid: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun unfollow(followedUid: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowers(uid: String): Resource<List<UserModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowings(uid: String): Resource<List<UserModel>> {
        TODO("Not yet implemented")
    }

}