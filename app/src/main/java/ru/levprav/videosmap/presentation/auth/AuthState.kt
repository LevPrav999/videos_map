package ru.levprav.videosmap.presentation.auth

data class AuthState(
    val data: AuthData? = AuthData(null, null, null),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class AuthData(
    val email: String?,
    val password: String?,
    val passwordConfirm: String?,

    )