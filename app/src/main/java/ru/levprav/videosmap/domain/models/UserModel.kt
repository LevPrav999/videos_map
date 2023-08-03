package ru.levprav.videosmap.domain.models

data class UserModel(
    val id: String, // ID of user (will be taken from auth)
    val name: String, // Name of User
    val description: String, // User Bio
    val imageUrl: String, // User Avatar Url
    val isFollowing: Boolean, // Is current user following this user
    val followers: MutableSet<String>, // List of User's followers
    val following: MutableSet<String>, // List of User's following
    val likeCount: Int, // Count of User videos' likes
)
