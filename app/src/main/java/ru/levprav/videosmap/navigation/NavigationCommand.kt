package ru.levprav.videosmap.navigation

import androidx.navigation.NamedNavArgument

interface NavigationCommand {

    val arguments: List<NamedNavArgument>

    val destination: String

    val clearStack: Boolean
}