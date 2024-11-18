package ru.levprav.videosmap.presentation.video

import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.models.VideoModel

data class VideoPlayerState(
    val isLiked: Boolean = false,
    val isSubscribed: Boolean = false,
    val user: UserModel? = null,

    var data: VideoModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
