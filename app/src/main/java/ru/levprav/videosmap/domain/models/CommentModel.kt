package ru.levprav.videosmap.domain.models

import java.util.Date

data class CommentModel(
    val id: String, // Id of comment
    val text: String, // Text of comment
    val createdAt: Date, // Date of comment created
    val videoId: String, // Video Id that has been commented
    val userId: String // User Id that posted comment
)

fun CommentModel.toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "text" to text,
        "createdAt" to createdAt,
        "videoId" to videoId,
        "userId" to userId
    )
}

fun Map<String, Any?>.toCommentModel(): CommentModel {
    return CommentModel(
        id = this["id"] as String,
        text = this["text"] as String,
        createdAt = this["createdAt"] as? Date ?: Date(),
        videoId = this["videoId"] as String,
        userId = this["userId"] as String
    )
}