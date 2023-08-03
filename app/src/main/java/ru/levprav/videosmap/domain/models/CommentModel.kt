package ru.levprav.videosmap.domain.models

import java.util.Date

data class CommentModel(
    val id: String,
    val text: String,
    val createdAt: Date,
    val videoId: String,
    val userId: String
)