package ru.levprav.videosmap.data.remote

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.domain.models.toMap
import java.io.File
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
            val file = File(fileUri)
            val uploadTask = imageRef.putFile(Uri.fromFile(file))

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
}