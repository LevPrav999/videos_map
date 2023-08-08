package ru.levprav.videosmap.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val signUpLiveData = MutableLiveData<Resource<Unit>>()
    private val signInLiveData = MutableLiveData<Resource<Unit>>()
    override suspend fun signUp(email: String, password: String, passwordConfirm: String): LiveData<Resource<Unit>> {
        try {
            if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                signUpLiveData.postValue(Resource.Error("Please fill in all fields"))
            }

            else if (password != passwordConfirm){
                signUpLiveData.postValue(Resource.Error("Passwords don't match"))
            }

            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signUpLiveData.postValue(Resource.Error("Incorrect Email format"))
            }else{
                val isUserExists = api.checkUserExists(email)
                if (isUserExists){
                    signUpLiveData.postValue(Resource.Error("Email is being used by another account"))
                }else{
                    api.signUp(email, password)
                    signUpLiveData.postValue(Resource.Success(Unit))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            signUpLiveData.postValue(Resource.Error(e.message ?: "Unknown error"))
        }

        return signUpLiveData
    }

    override suspend fun signIn(email: String, password: String): LiveData<Resource<Unit>> {
        try {
            if (email.isEmpty() || password.isEmpty()) {
                signInLiveData.postValue(Resource.Error("Please fill in all fields"))
            }

            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signInLiveData.postValue(Resource.Error("Incorrect Email format"))
            }else{
                val isUserExists = api.checkUserExists(email)
                if (!isUserExists){
                    signInLiveData.postValue(Resource.Error("User with this Email doesn't exists"))
                }else{
                    api.signIn(email, password).addOnCompleteListener{
                        task ->
                        if(task.isSuccessful){
                            signInLiveData.postValue(Resource.Success(Unit))
                        }else{
                            signInLiveData.postValue(Resource.Error(task.exception?.message.toString()))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            signInLiveData.postValue(Resource.Error(e.message ?: "Unknown error"))
        }

        return signInLiveData
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