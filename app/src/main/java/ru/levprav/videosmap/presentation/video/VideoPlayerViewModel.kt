package ru.levprav.videosmap.presentation.video

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.repository.VideoRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.navigation.AnotherProfileDestination
import ru.levprav.videosmap.navigation.CommentsNavigation
import ru.levprav.videosmap.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val userRepository: UserRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {
    var state by mutableStateOf(VideoPlayerState())
        private set

    fun loadVideoModel(videoId: String) {

        viewModelScope.launch {
            videoRepository.getVideoById(videoId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state = state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        state = state.copy(
                            data = result.data,
                            isLiked = result.data!!.liked.contains(userRepository.getCurrentUserId())
                        )
                        checkFollowing()
                        loadAvatar()
                    }
                }
            }
        }
    }

    private fun loadAvatar() {
        viewModelScope.launch {
            userRepository.getProfileDetail(state.data!!.userId).collect { result ->
                state = when (result) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        state.copy(
                            user = result.data!!
                        )
                    }
                }
            }
        }
    }

    private fun checkFollowing() {
        viewModelScope.launch {
            userRepository.getFollowings(userRepository.getCurrentUserId()!!).collect { result ->
                val currentUser = userRepository.getMyProfile()
                state = when (result) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        state.copy(
                            isSubscribed = result.data!!.toString()
                                .contains(currentUser.last().data!!.id)
                        )
                    }
                }
            }
        }
    }


    fun setLike() {
        state = state.copy(isLiked = state.data!!.liked.contains(userRepository.getCurrentUserId()))
    }

    fun like(videoId: String) {
        viewModelScope.launch {
            videoRepository.like(videoId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state = state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        state = state.copy(isLoading = false, data = result.data)
                        setLike()
                    }
                }
            }

        }
    }

    fun unlike(videoId: String) {
        viewModelScope.launch {
            videoRepository.unlike(videoId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state = state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        state = state.copy(isLoading = false, data = result.data)
                        setLike()
                    }
                }
            }

        }
    }

    fun follow(userId: String) {
        viewModelScope.launch {
            userRepository.follow(userId).collect { result ->
                if (result is Resource.Success) {
                    checkFollowing()
                }
            }
        }
    }

    fun navigateBack() {
        navigationManager.back()
    }

    fun navigateToComments(videoId: String) {
        navigationManager.navigate(CommentsNavigation.commentsScreen(videoId))
    }

    fun navigateToUser(userId: String) {
        navigationManager.navigate(AnotherProfileDestination.anotherProfileScreen(userId))
    }
}