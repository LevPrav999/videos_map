package ru.levprav.videosmap.data.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.models.toMap
import ru.levprav.videosmap.domain.models.toUserModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserApi @Inject constructor() {

    var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()


    suspend fun signUp(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun signIn(email: String, password: String): FirebaseUser? =
        withContext(Dispatchers.IO) {
            val signInTask = firebaseAuth.signInWithEmailAndPassword(email, password)

            suspendCoroutine { continuation ->
                signInTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result.user)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error Sign In")
                        )
                    }
                }
            }
        }


    suspend fun addUserDocument(user: UserModel) = withContext(Dispatchers.IO) {
        firebaseFirestore.collection("users").document(user.id).set(user.toMap())
    }

    suspend fun updateUserDocument(user: UserModel) = withContext(Dispatchers.IO) {
        firebaseFirestore.collection("users").document(user.id).update(user.toMap())
    }

    suspend fun getUserDocumentById(uid: String): UserModel = withContext(Dispatchers.IO) {


        suspendCoroutine { continuation ->
            firebaseFirestore.collection("users").document(uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snapshot = task.result
                        val user = snapshot?.data?.toUserModel()
                        if (user == null) {
                            continuation.resumeWithException(
                                task.exception ?: Exception("User not found")
                            )
                        } else {
                            continuation.resume(user)
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error getting user")
                        )
                    }
                }
        }
    }

    suspend fun saveUserAvatar(storagePath: String, image: Uri): String =
        withContext(Dispatchers.IO) {
            val storageReference = firebaseStorage.reference

            val imageRef = storageReference.child(storagePath)
            val uploadTask = imageRef.putFile(image)

            suspendCoroutine { continuation ->
                uploadTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snapshot = task.result
                        snapshot?.storage?.downloadUrl?.addOnCompleteListener { downloadUrlTask ->
                            if (downloadUrlTask.isSuccessful) {
                                val downloadUrl = downloadUrlTask.result.toString()
                                continuation.resume(downloadUrl)
                            } else {
                                continuation.resumeWithException(
                                    downloadUrlTask.exception
                                        ?: Exception("Error getting download URL")
                                )
                            }
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error uploading file")
                        )
                    }
                }
            }
        }

    suspend fun checkUserAuth(email: String): Boolean = withContext(Dispatchers.IO) {
        val fetchTask = firebaseAuth.fetchSignInMethodsForEmail(email)

        suspendCoroutine { continuation ->
            fetchTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result?.signInMethods?.size != 0)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Error checking auth")
                    )
                }
            }
        }
    }

    suspend fun checkUserDocumentExists(uid: String): Boolean = withContext(Dispatchers.IO) {
        val fetchTask = firebaseFirestore.collection("users").document(uid).get()

        suspendCoroutine { continuation ->
            fetchTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result?.data != null)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Error checking user documents")
                    )
                }
            }
        }
    }

}