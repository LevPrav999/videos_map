package ru.levprav.videosmap.data.repository

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import ru.levprav.videosmap.data.remote.UserApi
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
): UserRepository {
    override suspend fun signUp(email: String, password: String): Resource<Unit> {
        try {
            if (email.isEmpty() || password.isEmpty()) {
                return Resource.Error("Please fill in all fields")
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Resource.Error("Incorrect Email format")
            }

            val isUserExists = api.checkUserExists(email)
            if(!isUserExists){
                return Resource.Error("Email is being used by another account")
            }
            api.signUp(email, password)
            return Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun signIn(email: String, password: String): Resource<Unit>{
        return try{
            Resource.Success(
                data = api.signIn(email, password)
            )
        } catch (e: Exception){
            e.printStackTrace()
            if(e is FirebaseAuthInvalidCredentialsException){
                Resource.Error("Пользователя с такими данными не существует")
            }else{
                Resource.Error(e.message ?: "Неизвестная ошибка")
            }
        }
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