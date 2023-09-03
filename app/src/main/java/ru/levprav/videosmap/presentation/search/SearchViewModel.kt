package ru.levprav.videosmap.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.VideoRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.navigation.NavigationManager
import ru.levprav.videosmap.navigation.PlayerNavigation
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {
    var state by mutableStateOf(SearchState())
        private set

    init {
        getNewVideos()
    }

    fun getNewVideos() {
        viewModelScope.launch {
            videoRepository.getNewVideos().collect { result ->
                state = when (result) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        state.copy(
                            isLoading = false,
                            data = result.data!!
                        )
                    }
                }
            }
        }
    }

    fun onValueChanged(value: String) {
        state = state.copy(textFieldValue = value)
        if (value == "") {
            getNewVideos()
        } else {
            viewModelScope.launch {
                videoRepository.searchVideo(value).collect { result ->
                    state = when (result) {
                        is Resource.Loading -> {
                            state.copy(isLoading = true)
                        }

                        is Resource.Error -> {
                            state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state.copy(
                                isLoading = false,
                                data = result.data!!
                            )
                        }
                    }
                }
            }
        }
    }

    fun onVideoClicked(videoId: String) {
        navigationManager.navigate(PlayerNavigation.playerScreen(videoId = videoId))
    }
}