package ru.levprav.videosmap.domain.repository

import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.util.Resource

interface UserRepository {

    suspend fun signUp(email: String, password: String): Resource<String>
    suspend fun signIn(email: String, password: String): Resource<String>

    suspend fun getMyProfile(): Resource<UserModel>

    suspend fun getProfileDetail(targetUid: String): Resource<UserModel>

    suspend fun saveProfile(profile: UserModel): Resource<String>

    suspend fun follow(followedUid: String): Resource<String>

    suspend fun unfollow(followedUid: String): Resource<String>

    suspend fun getFollowers(uid: String): Resource<List<UserModel>>
    suspend fun getFollowings(uid: String): Resource<List<UserModel>>
}