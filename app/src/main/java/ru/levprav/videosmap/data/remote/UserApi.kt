package ru.levprav.videosmap.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.levprav.videosmap.domain.models.UserModel

class UserApi {

    var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }
}