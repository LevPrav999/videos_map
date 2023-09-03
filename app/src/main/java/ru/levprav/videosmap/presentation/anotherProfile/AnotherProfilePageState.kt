package ru.levprav.videosmap.presentation.anotherProfile

import ru.levprav.videosmap.domain.models.VideoModel

data class AnotherProfilePageState(
    val data: UserInfo = UserInfo(null, null, null),
    val videos: List<VideoModel>? = mutableListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
)


data class UserInfo(
    val username: String?,
    val description: String?,
    val imageUrl: String?,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val likesCount: Int = 0,

    val isFollowed: Boolean = false,
)