package ru.levprav.videosmap.data.remote

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.domain.models.toMap
import ru.levprav.videosmap.domain.models.toVideoModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VideoApi @Inject constructor() {

    private var _firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()

    suspend fun saveVideoToFirestore(video: VideoModel) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("videos").document(video.id).set(video.toMap())
    }

    suspend fun saveFileToStorage(storagePath: String, fileUri: String): String =
        withContext(Dispatchers.IO) {
            val storageReference = _firebaseStorage.reference

            val imageRef = storageReference.child(storagePath)
            val file = Uri.parse(fileUri)
            val uploadTask = imageRef.putFile(file)

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

    suspend fun saveBytesToStorage(storagePath: String, bytes: ByteArray): String =
        withContext(Dispatchers.IO) {
            val storageReference = _firebaseStorage.reference

            val imageRef = storageReference.child(storagePath)
            val uploadTask = imageRef.putBytes(bytes)

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

    suspend fun getVideosByUserId(id: String) = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            _firebaseFirestore.collection("videos").whereEqualTo("userId", id).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.documents.size != 0) {
                            val videos = mutableListOf<String>()
                            for (video in task.result.documents) {
                                videos.add(video.id)
                            }
                            continuation.resume(videos)
                        } else {
                            continuation.resumeWithException(
                                Exception("Videos data not found")
                            )
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error checking video documents")
                        )
                    }
                }

        }
    }

    suspend fun getVideoById(id: String) = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            _firebaseFirestore.collection("videos").document(id).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.data != null) {
                            continuation.resume(task.result.data?.toVideoModel())
                        } else {
                            continuation.resumeWithException(
                                Exception("Video data not found")
                            )
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error checking video documents")
                        )
                    }
                }

        }
    }

    suspend fun getVideoSnapshots(uid: String): Flow<QuerySnapshot> = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("videos").whereEqualTo("userId", uid).snapshots()
    }

    suspend fun like(videoId: String, currentUserId: String) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("videos").document(videoId)
            .update("liked", FieldValue.arrayUnion(currentUserId))
    }

    suspend fun unlike(videoId: String, currentUserId: String) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("videos").document(videoId)
            .update("liked", FieldValue.arrayRemove(currentUserId))
    }

    suspend fun deleteVideo(videoId: String) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("videos").document(videoId).delete()
    }

    suspend fun getNewVideos() = withContext(Dispatchers.IO) {

        suspendCoroutine { continuation ->
            _firebaseFirestore.collection("videos").orderBy("createdAt").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.documents.size != 0) {
                            val videos = mutableListOf<VideoModel>()
                            for (video in task.result.documents) {
                                videos.add(video.data!!.toVideoModel())
                            }
                            continuation.resume(videos)
                        } else {
                            continuation.resumeWithException(
                                Exception("Videos data not found")
                            )
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error checking video documents")
                        )
                    }
                }

        }
    }

    suspend fun getVideosByDescriptionContains(text: String) = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            _firebaseFirestore.collection("videos")
                .whereArrayContains("descriptionArray", text.lowercase()).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.documents.size != 0) {
                            val videos = mutableListOf<VideoModel>()
                            for (video in task.result.documents) {
                                videos.add(video.data!!.toVideoModel())
                            }
                            continuation.resume(videos)
                        } else {
                            continuation.resumeWithException(
                                Exception("Videos data not found")
                            )
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error checking video documents")
                        )
                    }
                }

        }
    }
}