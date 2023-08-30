package ru.levprav.videosmap.presentation.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.data.repository.VideoRepositoryImpl
import ru.levprav.videosmap.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class VideoDetailsViewModel @Inject constructor(
    private val repository: VideoRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(VideoDetailsState())
        private set

    fun saveVideo(uri: String, bytes: ByteArray, description: String) {
        viewModelScope.launch {
            repository.saveVideo(uri, bytes, description).collect { result ->
                state = when (result) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true, error = null)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, error = result.message)
                    }

                    else -> {
                        state.copy(isLoading = false, error = null)
                    }
                }
            }
        }
    }
}