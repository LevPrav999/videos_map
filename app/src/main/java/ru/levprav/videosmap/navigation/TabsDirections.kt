package ru.levprav.videosmap.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object TabsDirections {
    val map = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "map"

        override val clearStack = false

    }

    val search = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "search"

        override val clearStack = false

    }

    val add = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "add"

        override val clearStack = false

    }

    val notifications = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "notifications"

        override val clearStack = false

    }

    val profile = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "profile"

        override val clearStack = false

    }
}