package ru.levprav.videosmap.navigation


import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.levprav.videosmap.navigation.NavigationDirections.default

class NavigationManager(private val externalScope: CoroutineScope) {
    var commands = MutableSharedFlow<NavigationCommand>()

    fun navigate(
        directions: NavigationCommand
    ) {
        externalScope.launch {
            commands.emit(directions)
        }
    }
}