package ru.levprav.videosmap.domain.models

data class UserModel(
    val id: String, // ID of user (will be taken from auth)
    val name: String, // Name of User
    val description: String, // User Bio
    val imageUrl: String, // User Avatar Url
    val followers: List<String>, // List of User's followers
    val following: List<String>, // List of User's following
    val likeCount: Int, // Count of User videos' likes
)

fun UserModel.toMap(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "name" to name,
        "description" to description,
        "imageUrl" to imageUrl,
        "followers" to followers.toList(),
        "following" to following.toList(),
        "likeCount" to likeCount
    )
}

fun Map<String, Any>.toUserModel(): UserModel {
    return UserModel(
        id = this["id"].toString(),
        name = this["name"].toString(),
        description = this["description"].toString(),
        imageUrl = this["imageUrl"].toString(),
        followers = (this["followers"] as List<*>).map { it as String },
        following = (this["following"] as List<*>).map { it as String },
        likeCount = this["likeCount"].toString().toFloat().toInt()
    )
}
