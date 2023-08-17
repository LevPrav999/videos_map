package ru.levprav.videosmap.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var state by mutableStateOf(ProfilePageState())
        private set

    init {
        viewModelScope.launch {
            repository.getCurrentUserSnapshots().collect{
                result ->
                state = when(result){
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
    }
}