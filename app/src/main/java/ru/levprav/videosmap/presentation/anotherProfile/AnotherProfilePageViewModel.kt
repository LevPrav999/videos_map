package ru.levprav.videosmap.presentation.anotherProfile

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
class AnotherProfilePageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val videoRepository: VideoRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {
    var state by mutableStateOf(AnotherProfilePageState())
        private set


    fun loadProfile(userId: String) {
        viewModelScope.launch {
            userRepository.getProfileDetailSnapshots(userId).collect { result ->
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
                            isFollowed = result.data?.followers?.contains(userRepository.getCurrentUserId()!!)
                                ?: false
                        )
                        state.copy(
                            error = null,
                            data = data,
                        )
                    }
                }
            }

        }

        viewModelScope.launch {
            videoRepository.getVideosFromUidSnapshots(userId)
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
                                },
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    fun navigateToVideo(video: VideoModel) {
        navigationManager.navigate(PlayerNavigation.playerScreen(video.id))
    }

    fun follow(userId: String) {
        viewModelScope.launch {
            userRepository.follow(userId).collect { result ->
                if (result is Resource.Success) {
                    var data = state.data
                    data = data.copy(isFollowed = true)
                    state = state.copy(data = data)
                }
            }
        }
    }

    fun unfollow(userId: String) {
        viewModelScope.launch {
            userRepository.unfollow(userId).collect { result ->
                if (result is Resource.Success) {
                    var data = state.data
                    data = data.copy(isFollowed = false)
                    state = state.copy(data = data)
                }
            }
        }
    }
}