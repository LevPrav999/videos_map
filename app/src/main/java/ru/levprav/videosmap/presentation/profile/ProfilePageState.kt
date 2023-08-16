package ru.levprav.videosmap.presentation.profile

import android.net.Uri

data class ProfilePageState(
    val data: UserInfo = UserInfo(null, null, null),
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
)