package ru.levprav.videosmap.domain.repository

import android.content.Context
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.Flow
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.domain.util.Resource
import java.io.File

interface VideoRepository {
    suspend fun getVideosFromLocation(location: LatLng): Resource<List<VideoModel>>

    suspend fun getVideosInBoundingBox(bounds: LatLngBounds): Resource<List<VideoModel>>

    suspend fun getVideosFromUid(uid: String): Flow<Resource<List<VideoModel>>>
    suspend fun getVideosFromUidSnapshots(uid: String): Flow<Resource<List<VideoModel>>>

    suspend fun saveVideo(uri: String, byteArray: ByteArray, description: String): Flow<Resource<Unit>>
    suspend fun like(videoId: String): Flow<Resource<Unit>>
    suspend fun unlike(videoId: String): Flow<Resource<Unit>>
    suspend fun deleteVideo(videoId: String): Flow<Resource<Unit>>

    suspend fun searchVideo(text: String): Flow<Resource<List<VideoModel>>>

    suspend fun shareVideo(videoId: String): Resource<String>
    suspend fun getVideoFile(): Resource<File>
    suspend fun getVideoLocation(videoPath: String): Resource<LatLng>
    suspend fun getNewVideos(): Resource<List<VideoModel>>

}