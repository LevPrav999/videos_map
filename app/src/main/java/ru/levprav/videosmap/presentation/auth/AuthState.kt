package ru.levprav.videosmap.presentation.auth

data class AuthState(
    val toEditInfo: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)