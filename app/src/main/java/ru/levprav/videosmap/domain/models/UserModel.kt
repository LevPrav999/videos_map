package ru.levprav.videosmap.domain.models

data class UserModel(
    val id: String, // ID of user (will be taken from auth)
    val name: String, // Name of User
    val description: String, // User Bio
    val imageUrl: String, // User Avatar Url
    val isFollowing: Boolean, // Is current user following this user
    val followers: Set<Char>, // List of User's followers
    val following: Set<Char>, // List of User's following
    val likeCount: Int, // Count of User videos' likes
)

fun UserModel.toMap(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "name" to name,
        "description" to description,
        "imageUrl" to imageUrl,
        "isFollowing" to isFollowing,
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
        isFollowing = this["isFollowing"].toString().toBoolean(),
        followers = this["followers"].toString().toSet(),
        following = this["following"].toString().toSet(),
        likeCount = this["likeCount"].toString().toInt()
    )
}
