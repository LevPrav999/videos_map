package ru.levprav.videosmap.presentation.edituser

import android.net.Uri

data class EditUserState(
    val data: UserInfo = UserInfo(null, null, null),

    val isLoading: Boolean = false,
    val error: String? = null,

    val completed: Boolean = false
)


data class UserInfo(
    val username: String?,
    val description: String?,
    val imageUrl: Uri?
)