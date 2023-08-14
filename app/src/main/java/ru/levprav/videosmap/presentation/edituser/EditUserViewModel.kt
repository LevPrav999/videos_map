package ru.levprav.videosmap.presentation.edituser

import android.net.Uri
import android.util.Log
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
class EditUserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var state by mutableStateOf(EditUserState())
        private set

    fun getAvatar(avatar: Uri) {
        val data = state.data.copy(imageUrl = avatar)
        state = state.copy(data = data)
    }

    fun onDescriptionChange(description: String) {
        val data = state.data.copy(description = description)
        state = state.copy(data = data)
    }

    fun onUsernameChange(username: String) {
        val data = state.data.copy(username = username)
        state = state.copy(data = data)
    }

    fun submit() {
        // Will be call to user repository
        state.data.imageUrl?.path?.let { Log.d("submit", it) }
        state.data.description?.let { Log.d("submit", it) }
        state.data.username?.let { Log.d("submit", it) }

        viewModelScope.launch {
            state = state.copy(isLoading = true)

            repository.saveProfile(name = state.data.username!!, description = state.data.description!!, imageUrl = state.data.imageUrl!!, null, null, null, null)
                .collect { result ->
                    state = when (result) {
                        is Resource.Error -> {
                            state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state.copy(isLoading = false, error = null, completed = true)
                        }

                        else -> {
                            state.copy(isLoading = true)
                        }
                    }
                }
        }
    }

    fun navigate() {
        state = state.copy(completed = false)
    }
}