package ru.levprav.videosmap.presentation.comments

import ru.levprav.videosmap.domain.models.CommentModel
import ru.levprav.videosmap.domain.models.UserModel

data class CommentsState(
    val comments: List<CommentModel>? = null,
    val users: MutableList<UserModel> = mutableListOf(),

    val commentValue: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
)