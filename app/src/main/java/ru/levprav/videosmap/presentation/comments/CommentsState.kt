package ru.levprav.videosmap.presentation.comments

import ru.levprav.videosmap.domain.models.CommentModel

data class CommentsState(
    val comments: List<CommentModel>? = null,
    val avatars: MutableList<String>? = mutableListOf(),

    val commentValue: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
)