package ru.levprav.videosmap.navigation


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

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