package ru.levprav.videosmap.presentation.user

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.presentation.auth.AuthState
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    var state by mutableStateOf(EditUserState())
        private set

    fun getAvatar(avatar: Uri){
        val data = state.data.copy(imageUrl = avatar)
        state = state.copy(data = data)
    }
    fun onDescriptionChange(description: String){
        val data = state.data.copy(description = description)
        state = state.copy(data = data)
    }

    fun onUsernameChange(username: String){
        val data = state.data.copy(username = username)
        state = state.copy(data = data)
    }

    fun submit(){
        // Will be call to user repository
        state.data.imageUrl?.path?.let { Log.d("submit", it) }
        state.data.description?.let { Log.d("submit", it) }
        state.data.username?.let { Log.d("submit", it) }
    }
}