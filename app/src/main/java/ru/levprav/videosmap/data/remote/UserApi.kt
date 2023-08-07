package ru.levprav.videosmap.data.remote

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.UserModel
import javax.inject.Inject

class UserApi @Inject constructor(){

    var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun checkUserExists(email: String): Boolean = withContext(Dispatchers.IO) {
        val task = firebaseAuth.fetchSignInMethodsForEmail(email)
        Tasks.await(task)
        task.result?.signInMethods?.size == 0
    }


    suspend fun signIn(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }
}