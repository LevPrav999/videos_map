package ru.levprav.videosmap.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.levprav.videosmap.data.location.DefaultLocationTracker
import ru.levprav.videosmap.data.remote.UserApi
import ru.levprav.videosmap.data.remote.VideoApi
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.domain.repository.VideoRepository
import ru.levprav.videosmap.domain.util.Resource
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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

    override suspend fun saveVideo(context: Context, uri: Uri): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val location = locationTracker.getCurrentLocation()
        if(location == null){
            emit(Resource.Error("Location not found"))
        }else{
            val time = System.currentTimeMillis()
                val videoPath = userApi.getCurrentUserId() + "/${time}.${
                    uri.path?.split(".")?.last()
                }"
            val imagePath = userApi.getCurrentUserId() + "/image-${time}.${
                uri.path?.split(".")?.last()
            }"
            try{
                videoApi.saveFileToStorage(videoPath, uri)
                videoApi.saveBytesToStorage(imagePath, createVideoThumbnail(context, uri)!!)
                emit(Resource.Success(Unit))
            }catch (e: Exception){
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

    fun createVideoThumbnail(context: Context, videoUri: Uri): ByteArray? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, videoUri)
            val thumbnailBitmap = retriever.frameAtTime
            val outputStream = ByteArrayOutputStream()
            thumbnailBitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
    }

}