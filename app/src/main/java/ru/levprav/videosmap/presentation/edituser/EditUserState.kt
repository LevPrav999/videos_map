package ru.levprav.videosmap.presentation.edituser

import android.net.Uri

data class EditUserState(
    val data: UserInfo = UserInfo(null, null, null, null),
    val isFromNetwork: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,

)


data class UserInfo(
    val username: String?,
    val description: String?,
    val imageUrl: Uri?,
    val imageUrlNetwork: String?,
)