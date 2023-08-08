package ru.levprav.videosmap.presentation.signUp

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null
)