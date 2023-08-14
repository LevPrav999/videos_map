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
import ru.levprav.videosmap.navigation.NavigationDirections
import ru.levprav.videosmap.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {
    var state by mutableStateOf(EditUserState())
        private set

    init {
        viewModelScope.launch {
            repository.getMyProfile()
                .collect { result ->
                    state = when (result) {

                        is Resource.Success -> {
                            val data = state.data.copy(username = result.data?.name, description = result.data?.description, imageUrlNetwork = result.data?.imageUrl)
                            state.copy(isLoading = false, error = null, data = data, isFromNetwork = true)
                        }

                        is Resource.Loading -> {
                            state.copy(isLoading = true)
                        }

                        is Resource.Error -> {
                            state.copy(isLoading = false, error = result.message)
                        }
                    }
                }
        }
    }

    fun getAvatar(avatar: Uri) {
        val data = state.data.copy(imageUrl = avatar)
        state = state.copy(data = data, isFromNetwork = false)
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

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.saveProfile(name = state.data.username!!, description = state.data.description!!, localUri = state.data.imageUrl, networkUrl = state.data.imageUrlNetwork, null, null, null, null)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            state = state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state = state.copy(isLoading = false, error = null)
                            navigationManager.navigate(NavigationDirections.mainScreen)
                        }

                        else -> {
                            state = state.copy(isLoading = true)
                        }
                    }
                }
        }
    }

}