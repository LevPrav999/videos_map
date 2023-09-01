package ru.levprav.videosmap.domain.models

import java.util.Date

data class CommentModel(
    val id: String, // Id of comment
    val text: String, // Text of comment
    val createdAt: Date, // Date of comment created
    val videoId: String, // Video Id that has been commented
    val userId: String // User Id that posted comment
)