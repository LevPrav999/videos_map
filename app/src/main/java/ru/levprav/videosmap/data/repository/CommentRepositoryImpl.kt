package ru.levprav.videosmap.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.levprav.videosmap.data.remote.CommentApi
import ru.levprav.videosmap.domain.models.CommentModel
import ru.levprav.videosmap.domain.models.toCommentModel
import ru.levprav.videosmap.domain.repository.CommentRepository
import ru.levprav.videosmap.domain.util.Resource
import java.util.Date
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentApi: CommentApi,
) : CommentRepository {
    override suspend fun post(text: String, userId: String, videoId: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            if (text.length < 3) {
                emit(Resource.Error("Minimum comment length is 3"))
            } else if (text.length > 20) {
                emit(Resource.Error("Maximum comment length is 20"))
            } else {
                val time = System.currentTimeMillis()
                val comment = CommentModel(
                    id = time.toString(),
                    text = text,
                    createdAt = Date(),
                    videoId = videoId,
                    userId = userId
                )

                commentApi.postComment(comment)
                emit(Resource.Success(Unit))
            }
        }

    override suspend fun getAllCommentsSnapshotsByVideoId(videoId: String): Flow<Resource<List<CommentModel>>> =
        flow {
            emit(Resource.Loading())
            try {
                commentApi.getCommentsSnapshots(videoId).collect { snapshot ->
                    if (snapshot.documents.size != 0 && !snapshot.isEmpty) {
                        val comments = mutableListOf<CommentModel>()
                        for (video in snapshot.documents) {
                            if (video.data != null) {
                                comments.add(video.data!!.toCommentModel())
                            }
                        }
                        emit(Resource.Success(comments))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
}