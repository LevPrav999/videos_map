package ru.levprav.videosmap.presentation.video

import ru.levprav.videosmap.domain.models.VideoModel

data class VideoPlayerState(
    val isLiked: Boolean = false,
    val isSubscribed: Boolean = false,
    val avatar: String? = null,

    val data: VideoModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
