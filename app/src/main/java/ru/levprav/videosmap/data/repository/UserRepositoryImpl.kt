package ru.levprav.videosmap.data.repository

import android.net.Uri
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
            val isUserExists = api.checkUserAuth(email)
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
            val isUserExists = api.checkUserAuth(email)
            if (!isUserExists) {
                emit(Resource.Error("User with this Email doesn't exists"))
            } else {
                try {
                    val user = api.signIn(email, password)
                    if (user != null) {
                        emit(Resource.Success(Unit))
                    } else {
                        emit(Resource.Error("Wrong password"))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMyProfile(): Flow<Resource<UserModel>> = flow<Resource<UserModel>> {
        emit(Resource.Loading())
        api.firebaseAuth.currentUser?.let {
            val result = api.getUserDocumentById(it.uid)
            emit(Resource.Success(result))
        } ?: emit(Resource.Error("User not found"))
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfileDetail(targetUid: String): Resource<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun saveProfile(
        name: String?,
        description: String?,
        localUri: Uri?,
        networkUrl: String?,
        isFollowing: Boolean?,
        followers: List<String>?,
        following: List<String>?,
        likeCount: Int?
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val id = api.firebaseAuth.currentUser!!.uid
        if (api.checkUserDocumentExists(id)) {

            val oldUser = api.getUserDocumentById(id)

            val avatar = if (localUri != null) {
                api.saveUserAvatar("profilePictures/$id", localUri)
            } else {
                networkUrl!!
            }

            val user = UserModel(
                id = id,
                name = name ?: oldUser.name,
                description = description ?: oldUser.description,
                imageUrl = avatar,
                isFollowing = isFollowing ?: oldUser.isFollowing,
                followers = followers ?: oldUser.followers,
                following = following ?: oldUser.following,
                likeCount = likeCount ?: oldUser.likeCount,
            )
            api.updateUserDocument(user)

        } else {
            val avatar = api.saveUserAvatar("profilePictures/$id", localUri!!)

            val user = UserModel(
                id = id,
                name = name!!,
                description = description!!,
                imageUrl = avatar,
                isFollowing = false,
                followers = listOf(),
                following = listOf(),
                likeCount = 0,
            )
            api.addUserDocument(user)
        }
        emit(Resource.Success(Unit))
    }.flowOn(Dispatchers.IO)

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