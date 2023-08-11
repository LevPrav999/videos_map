package ru.levprav.videosmap.data.repository

import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.levprav.videosmap.data.remote.UserApi
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.models.toUserModel
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
                    api.signIn(email, password)
                    emit(Resource.Success(Unit))
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
            Tasks.await(result)
            emit(Resource.Success(result.result.data?.toUserModel()))
        } ?: emit(Resource.Error("User not found"))
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfileDetail(targetUid: String): Resource<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun saveProfile(name: String?, description: String?, imageUrl: String?, isFollowing: Boolean?, followers: Set<Char>?, following: Set<Char>?, likeCount: Int?): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val id = api.firebaseAuth.currentUser!!.uid
        if(api.checkUserDocumentExists(id)){

            val task = api.getUserDocumentById(id)
            Tasks.await(task)
            val oldUser = task.result.data!!.toUserModel()

            val user = UserModel(
                id = id,
                name = name ?: oldUser.name,
                description = description ?: oldUser.description,
                imageUrl = imageUrl ?: oldUser.imageUrl,
                isFollowing = isFollowing ?: oldUser.isFollowing,
                followers = followers ?: oldUser.followers,
                following = following ?: oldUser.following,
                likeCount = likeCount ?: oldUser.likeCount,
            )
            api.updateUserDocument(user)

        }else{
            val user = UserModel(
                id = id,
                name = name!!,
                description = description!!,
                imageUrl = imageUrl!!,
                isFollowing = false,
                followers = setOf(),
                following = setOf(),
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