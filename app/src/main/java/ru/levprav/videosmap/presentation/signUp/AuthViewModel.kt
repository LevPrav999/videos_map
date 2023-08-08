package ru.levprav.videosmap.presentation.signUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.levprav.videosmap.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    fun signUp(email: String, password: String){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = when(val result = repository.signUp(email, password)){
                is Resource.Error -> {
                    state.copy(isLoading = false, error = result.message)
                }
                is Resource.Success -> {
                    state.copy(isLoading = false, error = null)
                    // redirect to EditUserInfo screen
                }
            }
        }
    }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = when(val result = repository.signIn(email, password)){
                is Resource.Error -> {
                    state.copy(isLoading = false, error = result.message)
                }
                is Resource.Success -> {
                    state.copy(isLoading = false, error = null)
                    // redirect to EditUserInfo screen
                }
            }
        }
    }
}