package ru.levprav.videosmap.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.util.Resource

interface UserRepository {

    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<Resource<Unit>>

    suspend fun signIn(email: String, password: String): Flow<Resource<Unit>>

    suspend fun getMyProfile(): Resource<UserModel>

    suspend fun getProfileDetail(targetUid: String): Resource<UserModel>

    suspend fun saveProfile(profile: UserModel): Resource<Unit>

    suspend fun follow(followedUid: String): Resource<Unit>

    suspend fun unfollow(followedUid: String): Resource<Unit>

    suspend fun getFollowers(uid: String): Resource<List<UserModel>>
    suspend fun getFollowings(uid: String): Resource<List<UserModel>>
}