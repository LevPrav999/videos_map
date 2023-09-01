package ru.levprav.videosmap.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.CommentModel
import ru.levprav.videosmap.domain.models.toMap
import javax.inject.Inject

class CommentApi @Inject constructor() {

    private var _firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getCommentsSnapshots(videoId: String): Flow<QuerySnapshot> =
        withContext(Dispatchers.IO) {
            _firebaseFirestore.collection("comments").whereEqualTo("videoId", videoId).snapshots()
        }

    suspend fun postComment(comment: CommentModel) = withContext(Dispatchers.IO) {
        _firebaseFirestore.collection("comments").document(comment.id).set(comment.toMap())
    }
}