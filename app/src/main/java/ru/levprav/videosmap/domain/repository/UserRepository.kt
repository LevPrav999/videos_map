package ru.levprav.videosmap.domain.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.util.Resource

interface UserRepository {

    suspend fun init(context: Context)

    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<Resource<Unit>>

    suspend fun signIn(email: String, password: String): Flow<Resource<Unit>>

    suspend fun getMyProfile(): Flow<Resource<UserModel>>

    suspend fun getProfileDetail(targetUid: String): Flow<Resource<UserModel>>

    suspend fun getProfileDetailSnapshots(targetUid: String): Flow<Resource<UserModel>>

    suspend fun saveProfile(
        name: String?,
        description: String?,
        localUri: String?,
        networkUrl: String?,
        isFollowing: Boolean?,
        followers: List<String>?,
        following: List<String>?,
        likeCount: Int?
    ): Flow<Resource<Unit>>

    suspend fun follow(followedUid: String): Flow<Resource<Unit>>

    suspend fun unfollow(followedUid: String): Flow<Resource<Unit>>

    suspend fun getFollowers(uid: String): Flow<Resource<List<UserModel>>>
    suspend fun getFollowings(uid: String): Flow<Resource<List<UserModel>>>

    suspend fun getCurrentUserSnapshots(): Flow<Resource<UserModel>>

    fun getCurrentUserId(): String?
}