package ru.levprav.videosmap.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.repository.VideoRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.navigation.NavigationManager
import ru.levprav.videosmap.navigation.PlayerNavigation
import javax.inject.Inject

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val videoRepository: VideoRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {
    var state by mutableStateOf(ProfilePageState())
        private set

    init {
        viewModelScope.launch {
            userRepository.getCurrentUserSnapshots().collect { result ->
                state = when (result) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        val data = state.data.copy(
                            username = result.data?.name,
                            description = result.data?.description,
                            imageUrl = result.data?.imageUrl,
                            followersCount = result.data?.followers?.size ?: 0,
                            followingCount = result.data?.following?.size ?: 0,
                            likesCount = result.data?.likeCount ?: 0,
                        )
                        state.copy(
                            isLoading = false,
                            error = null,
                            data = data,
                        )
                    }
                }
            }

        }

        viewModelScope.launch {
            videoRepository.getVideosFromUidSnapshots(userRepository.getCurrentUserId()!!)
                .collect { result ->
                    state = when (result) {
                        is Resource.Loading -> {
                            state.copy(isLoading = true)
                        }

                        is Resource.Error -> {
                            state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            state.copy(
                                videos = result.data?.sortedByDescending {
                                    it.createdAt
                                }
                            )
                        }
                    }
                }
        }
    }

    fun navigateToVideo(video: VideoModel) {
        navigationManager.navigate(PlayerNavigation.playerScreen(video.id))
    }
}