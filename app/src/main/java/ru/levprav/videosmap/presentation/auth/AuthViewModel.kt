package ru.levprav.videosmap.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.navigation.NavigationDirections
import ru.levprav.videosmap.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    fun signUp(email: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            repository.signUp(email, password, passwordConfirm)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            state = state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state = state.copy(isLoading = false, error = null)
                            navigationManager.navigate(NavigationDirections.editUser)
                        }

                        else -> {
                            state = state.copy(isLoading = true)
                        }
                    }
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            repository.signIn(email, password)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            state = state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state = state.copy(isLoading = false, error = null, data = null)
                            navigationManager.navigate(NavigationDirections.editUser)

                        }

                        else -> {
                            state = state.copy(isLoading = true)
                        }
                    }
                }
        }
    }


    fun onEmailChanged(email: String) {
        val data = state.data?.copy(email = email)
        state = state.copy(data = data, error = null)
    }

    fun onPasswordChanged(password: String) {
        val data = state.data?.copy(password = password)
        state = state.copy(data = data, error = null)
    }

    fun onPasswordConfirmChanged(passwordConfirm: String) {
        val data = state.data?.copy(passwordConfirm = passwordConfirm)
        state = state.copy(data = data, error = null)
    }
}