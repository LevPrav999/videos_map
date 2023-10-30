package ru.levprav.videosmap.presentation.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.navigation.NavigationDirections
import ru.levprav.videosmap.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {

    fun initAppwrite(context: Context) {
        viewModelScope.launch {
            repository.init(context)

            if (repository.getCurrentUserId() != null) {
                navigationManager.navigate(NavigationDirections.mainScreen)
            } else {
                navigationManager.navigate(NavigationDirections.authentication)
            }
        }
    }
}