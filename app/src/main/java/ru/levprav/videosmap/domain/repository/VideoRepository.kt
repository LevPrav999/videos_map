package ru.levprav.videosmap.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.domain.util.Resource
import java.io.File

interface VideoRepository {
    suspend fun getVideosFromLocation(location: LatLng): Resource<List<VideoModel>>

    suspend fun getVideosInBoundingBox(bounds: LatLngBounds): Resource<List<VideoModel>>

    suspend fun getVideosFromUid(uid: String): Resource<List<VideoModel>>

    suspend fun saveVideo(video: VideoModel): Resource<String>
    suspend fun like(video: VideoModel): Resource<String>
    suspend fun unlike(video: VideoModel): Resource<String>
    suspend fun deleteVideo(videoId: String): Resource<String>

    suspend fun searchVideo(videoId: String): Resource<List<VideoModel>>

    suspend fun shareVideo(videoId: String): Resource<String>
    suspend fun getVideoFile(): Resource<File>
    suspend fun getVideoLocation(videoPath: String): Resource<LatLng>
    suspend fun getNewVideos(): Resource<List<VideoModel>>

}