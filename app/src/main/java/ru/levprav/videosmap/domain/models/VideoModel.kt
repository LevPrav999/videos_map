package ru.levprav.videosmap.domain.models

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.Date

data class VideoModel(
    val id: String, // Video's ID
    val url: String, // URL of the video in full size
    val imageUrl: String, // URL of the image of the first frame of the video in full size
    val thumbnailUrl: String, // URL of the thumbnail of the first frame of the video
    val createdAt: Date, // Timestamp of when the video was posted
    val description: String, // Text description of the video. Used to perform keyword search as well
    val userId: String, // ID of the user who have posted the video
    val isFollowing: Boolean, // Whether the logged in user is following the creator of this video
    val position: LatLng, // Coordinates of the position of the video
    val likeCount: Int, // Video likes count
    val commentCount: Int, // Video comments count
    val haveLiked: Boolean, // Whether the logged in user liked video
)