package ru.levprav.videosmap.presentation.signUp

data class SignUpState(
    val data: List<String> = listOf("", ""),
    val isLoading: Boolean = false,
    val error: String? = null
)