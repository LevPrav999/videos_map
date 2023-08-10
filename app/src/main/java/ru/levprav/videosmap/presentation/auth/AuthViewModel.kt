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
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    fun signUp(email: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            repository.signUp(email, password, passwordConfirm)
                .collect { result ->
                    state = when (result) {
                        is Resource.Error -> {
                            state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state.copy(isLoading = false, error = null, toEditInfo = true)

                        }

                        else -> {
                            state.copy(isLoading = true)
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
                    state = when (result) {
                        is Resource.Error -> {
                            state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state.copy(isLoading = false, error = null, toEditInfo = true)
                        }

                        else -> {
                            state.copy(isLoading = true)
                        }
                    }
                }
        }
    }

    fun navigate() {
        state = state.copy(toEditInfo = false)
    }

    fun onEmailChanged(email: String){
        val data = state.data.copy(email = email)
        state = state.copy(data = data)
    }

    fun onPasswordChanged(password: String){
        val data = state.data.copy(password = password)
        state = state.copy(data = data)
    }

    fun onPasswordConfirmChanged(passwordConfirm: String){
        val data = state.data.copy(passwordConfirm = passwordConfirm)
        state = state.copy(data = data)
    }
}