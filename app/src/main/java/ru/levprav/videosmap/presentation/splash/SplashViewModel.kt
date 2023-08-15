package ru.levprav.videosmap.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.util.Resource
import ru.levprav.videosmap.navigation.NavigationDirections
import ru.levprav.videosmap.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {
    init {
        viewModelScope.launch{
            repository.getMyProfile().collect{  result ->
                when(result){
                    is Resource.Error -> {
                        navigationManager.navigate(NavigationDirections.authentication)
                    }
                    is Resource.Success -> {
                        navigationManager.navigate(NavigationDirections.mainScreen)
                    }
                    else -> {}
                }
            }
        }
    }
}