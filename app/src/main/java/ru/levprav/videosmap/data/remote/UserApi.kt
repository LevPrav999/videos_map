package ru.levprav.videosmap.data.remote

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.models.toMap
import ru.levprav.videosmap.domain.models.toUserModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserApi @Inject constructor() {


    private var _firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()


    suspend fun signUp(email: String, password: String) = withContext(Dispatchers.IO) {
        _firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun signIn(email: String, password: String): FirebaseUser? =
        withContext(Dispatchers.IO) {
            val signInTask = _firebaseAuth.signInWithEmailAndPassword(email, password)

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
        _firebaseFirestore.collection("users").document(user.id).set(user.toMap())
    }

    suspend fun updateUserDocument(user: UserModel) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("users").document(user.id).update(user.toMap())
    }

    suspend fun getUserDocumentById(uid: String): UserModel = withContext(Dispatchers.IO) {


        suspendCoroutine { continuation ->
            _firebaseFirestore.collection("users").document(uid).get()
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

    suspend fun updateUserLikesCount(userId: String, isLike: Boolean) =
        withContext(Dispatchers.IO) {
            _firebaseFirestore.collection("users").document(userId)
                .update("likeCount", FieldValue.increment(if (isLike) 1 else -1))
        }

    suspend fun getUserSnapshots(): Flow<DocumentSnapshot> = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("users").document(getCurrentUserId()!!).snapshots()
    }

    suspend fun getUserSnapshotsById(uid: String): Flow<DocumentSnapshot> =
        withContext(Dispatchers.IO) {
            _firebaseFirestore.collection("users").document(uid).snapshots()
        }

    suspend fun saveUserAvatar(storagePath: String, image: Uri): String =
        withContext(Dispatchers.IO) {
            val storageReference = _firebaseStorage.reference

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
        val fetchTask = _firebaseAuth.fetchSignInMethodsForEmail(email)

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
        val fetchTask = _firebaseFirestore.collection("users").document(uid).get()

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


    suspend fun follow(targetUid: String) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("users").document(targetUid)
            .update("followers", FieldValue.arrayUnion(getCurrentUserId()!!))
        _firebaseFirestore.collection("users").document(getCurrentUserId()!!)
            .update("following", FieldValue.arrayUnion(targetUid))
    }

    suspend fun unfollow(targetUid: String) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("users").document(targetUid)
            .update("followers", FieldValue.arrayRemove(getCurrentUserId()!!))
        _firebaseFirestore.collection("users").document(getCurrentUserId()!!)
            .update("following", FieldValue.arrayRemove(targetUid))
    }

    suspend fun getFollowers(uid: String) = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            _firebaseFirestore.collection("users").document(uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<UserModel>()
                        val user = task.result.data?.toUserModel()
                        if (user != null) {
                            for (userId in user.followers) {
                                val getTask =
                                    _firebaseFirestore.collection("users").document(uid).get()
                                Tasks.await(getTask)
                                val follower = getTask.result.data?.toUserModel()
                                if (follower != null) {
                                    list.add(follower)
                                }
                            }
                            continuation.resume(list)
                        } else {
                            continuation.resumeWithException(
                                Exception("User data not found")
                            )
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error checking user documents")
                        )
                    }
                }
        }
    }

    suspend fun getFollowings(uid: String) = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            _firebaseFirestore.collection("users").whereArrayContains("followers", uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<UserModel>()
                        for (doc in task.result.documents) {
                            if (doc.data != null) {
                                list.add(doc.data!!.toUserModel())
                            }
                        }
                        continuation.resume(list)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error checking user documents")
                        )
                    }
                }
        }
    }

    fun getCurrentUserId(): String? = _firebaseAuth.currentUser?.uid

}