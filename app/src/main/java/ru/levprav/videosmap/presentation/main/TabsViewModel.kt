package ru.levprav.videosmap.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.presentation.auth.AuthState
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var state by mutableStateOf(TabsState())
        private set

    init {
        viewModelScope.launch {
            repository.getMyProfile().collect{
                    result ->
                state = when (result) {
                    is Resource.Error -> {
                        state.copy(data = "error")
                    }

                    is Resource.Success -> {
                        result.data?.let { state.copy(data = it.name) } ?: state.copy(data = "not found")

                    }

                    else -> {
                        state.copy(data = "loading...")
                    }
                }
            }
        }
    }
}