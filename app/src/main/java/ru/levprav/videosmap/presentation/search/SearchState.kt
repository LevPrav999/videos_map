package ru.levprav.videosmap.presentation.search

import ru.levprav.videosmap.domain.models.VideoModel

data class SearchState(
    val data: List<VideoModel> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val textFieldValue: String? = null,
)