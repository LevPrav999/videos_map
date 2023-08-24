package ru.levprav.videosmap.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.levprav.videosmap.data.location.DefaultLocationTracker
import ru.levprav.videosmap.data.remote.UserApi
import ru.levprav.videosmap.data.remote.VideoApi
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.domain.repository.VideoRepository
import ru.levprav.videosmap.domain.util.Resource
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Date
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val videoApi: VideoApi,
    private val userApi: UserApi,
    private val locationTracker: DefaultLocationTracker
) : VideoRepository {
    override suspend fun getVideosFromLocation(location: LatLng): Resource<List<VideoModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideosInBoundingBox(bounds: LatLngBounds): Resource<List<VideoModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideosFromUid(uid: String): Resource<List<VideoModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveVideo(uri: String, byteArray: ByteArray, description: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val location = locationTracker.getCurrentLocation()
        if (location == null) {
            emit(Resource.Error("Location not found"))
        } else {
            val time = System.currentTimeMillis()
            val videoPath = userApi.getCurrentUserId() + "/${time}.${
                uri.split(".").last()
            }"
            val imagePath = userApi.getCurrentUserId() + "/image-${time}.${
                uri.split(".").last()
            }"
            val created = uri.split("/").last().replace(".mp4", "")

            try {
                val videoUrl = videoApi.saveFileToStorage(videoPath, uri)
                val thumbnailUrl = videoApi.saveBytesToStorage(imagePath, byteArray)

                val video = VideoModel(
                    id = time.toString(),
                    url = videoUrl,
                    thumbnailUrl = thumbnailUrl,
                    imageUrl = thumbnailUrl,
                    createdAt = Date(),
                    description = description,
                    userId = userApi.getCurrentUserId()!!,
                    position = location,
                    likeCount = 0,
                    commentCount = 0,
                    liked = listOf()
                )

                videoApi.saveVideoToFirestore(video)

                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error saving video"))
            }
        }
    }

    override suspend fun like(video: VideoModel): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun unlike(video: VideoModel): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVideo(videoId: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun searchVideo(videoId: String): Resource<List<VideoModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun shareVideo(videoId: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideoFile(): Resource<File> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideoLocation(videoPath: String): Resource<LatLng> {
        TODO("Not yet implemented")
    }

    override suspend fun getNewVideos(): Resource<List<VideoModel>> {
        TODO("Not yet implemented")
    }

}