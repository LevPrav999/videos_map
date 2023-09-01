package ru.levprav.videosmap.presentation.comments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.CommentRepository
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentRepository,
    private val userRepository: UserRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {
    var state by mutableStateOf(CommentsState())
        private set


    suspend fun loadComments(videoId: String) {
        viewModelScope.launch {
            repository.getAllCommentsSnapshotsByVideoId(videoId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Error -> {
                        state = state.copy(isLoading = false, error = result.message)
                    }

                    is Resource.Success -> {
                        state = state.copy(
                            comments = result.data,
                        )
                        getAvatarsFromComments()
                    }
                }
            }
        }
    }

    private suspend fun getAvatarsFromComments() {
        viewModelScope.launch {
            for (comment in state.comments!!) {
                userRepository.getProfileDetail(comment.userId).collect { result ->
                    when (result) {

                        is Resource.Error -> {
                            state = state.copy(isLoading = false, error = result.message)
                        }

                        is Resource.Success -> {
                            val array = state.avatars!!
                            array.add(result.data!!.imageUrl)
                            state = state.copy(
                                avatars = array,
                            )
                        }

                        else -> {}
                    }
                }
            }
            state = state.copy(isLoading = false)
        }
    }
}