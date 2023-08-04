package ru.levprav.videosmap.data.repository

import com.google.firebase.auth.FirebaseAuthUserCollisionException
import ru.levprav.videosmap.data.remote.UserApi
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: UserApi
): UserRepository {
    override suspend fun signUp(email: String, password: String): Resource<Unit>{
        return try{
            Resource.Success(
                data = api.signUp(email, password)
            )
        } catch (e: Exception){
            e.printStackTrace()
            if(e is FirebaseAuthUserCollisionException){
                Resource.Error("Пользователь с таким Email уже существует")
            }else{
                Resource.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Resource<Unit>{
        TODO("Not yet implemented")
    }

    override suspend fun getMyProfile(): Resource<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfileDetail(targetUid: String): Resource<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun saveProfile(profile: UserModel): Resource<Unit>{
        TODO("Not yet implemented")
    }

    override suspend fun follow(followedUid: String): Resource<Unit>{
        TODO("Not yet implemented")
    }

    override suspend fun unfollow(followedUid: String): Resource<Unit>{
        TODO("Not yet implemented")
    }

    override suspend fun getFollowers(uid: String): Resource<List<UserModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowings(uid: String): Resource<List<UserModel>> {
        TODO("Not yet implemented")
    }

}