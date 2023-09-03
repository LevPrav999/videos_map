package ru.levprav.videosmap.domain.models

import com.google.android.gms.maps.model.LatLng
import java.util.Date

data class VideoModel(
    val id: String, // Video's ID
    val url: String, // URL of the video in full size
    val imageUrl: String, // URL of the image of the first frame of the video in full size
    val thumbnailUrl: String, // URL of the thumbnail of the first frame of the video
    val createdAt: Date, // Timestamp of when the video was posted
    val description: String, // Text description of the video. Used to perform keyword search as well
    val descriptionArray: List<String>, // List of description words
    val userId: String, // ID of the user who have posted the video
    val position: LatLng, // Coordinates of the position of the video
    val commentCount: Int, // Video comments count
    val liked: List<String>, // Whether the logged in user liked video
)

fun VideoModel.toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "url" to url,
        "imageUrl" to imageUrl,
        "thumbnailUrl" to thumbnailUrl,
        "createdAt" to createdAt,
        "description" to description,
        "descriptionArray" to description,
        "userId" to userId,
        "position" to mapOf(
            "latitude" to position.latitude,
            "longitude" to position.longitude
        ),
        "commentCount" to commentCount,
        "liked" to liked
    )
}

fun Map<String, Any?>.toVideoModel(): VideoModel {
    val positionMap = this["position"] as? Map<*, *>
    val position = positionMap?.let {
        val latitude = it["latitude"] as? Double ?: 0.0
        val longitude = it["longitude"] as? Double ?: 0.0
        LatLng(latitude, longitude)
    } ?: LatLng(0.0, 0.0)

    return VideoModel(
        id = this["id"] as? String ?: "",
        url = this["url"] as? String ?: "",
        imageUrl = this["imageUrl"] as? String ?: "",
        thumbnailUrl = this["thumbnailUrl"] as? String ?: "",
        createdAt = this["createdAt"] as? Date ?: Date(),
        description = this["description"] as? String ?: "",
        descriptionArray = (this["descriptionArray"] as List<*>).map { it as String },
        userId = this["userId"] as? String ?: "",
        position = position,
        commentCount = this["commentCount"] as? Int ?: 0,
        liked = (this["liked"] as List<*>).map { it as String }
    )
}