package ru.levprav.videosmap.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.levprav.videosmap.domain.models.CommentModel
import ru.levprav.videosmap.domain.util.Resource

interface CommentRepository {
    suspend fun post(
        text: String,
        userId: String,
        videoId: String,
    ): Flow<Resource<Unit>>

    suspend fun getAllCommentsSnapshotsByVideoId(
        videoId: String,
    ): Flow<Resource<List<CommentModel>>>
}