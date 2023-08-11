package ru.levprav.videosmap.data.remote

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.models.toMap
import javax.inject.Inject

class UserApi @Inject constructor() {

    var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    suspend fun signUp(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun signIn(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }


    suspend fun addUserDocument(user: UserModel) = withContext(Dispatchers.IO){
        firebaseFirestore.collection("users").document(user.id).set(user.toMap())
    }

    suspend fun updateUserDocument(user: UserModel) = withContext(Dispatchers.IO){
        firebaseFirestore.collection("users").document(user.id).update(user.toMap())
    }

    suspend fun getUserDocumentById(uid: String) = withContext(Dispatchers.IO){
        firebaseFirestore.collection("users").document(uid).get()
    }

    suspend fun checkUserAuth(email: String): Boolean = withContext(Dispatchers.IO) {
        val task = firebaseAuth.fetchSignInMethodsForEmail(email)
        Tasks.await(task)
        task.result?.signInMethods?.size != 0
    }

    suspend fun checkUserDocumentExists(uid: String): Boolean = withContext(Dispatchers.IO) {
        val task = firebaseFirestore.collection("users").document(uid).get()
        Tasks.await(task)
        task.result?.data != null
    }

}