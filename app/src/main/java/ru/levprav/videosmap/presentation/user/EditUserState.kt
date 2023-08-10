package ru.levprav.videosmap.presentation.user

data class EditUserState(
    val data: UserInfo,

    val isLoading: Boolean = false,
    val error: String? = null
)


data class UserInfo(
    val name: String,
    val description: String,
    val imageUrl: String
)