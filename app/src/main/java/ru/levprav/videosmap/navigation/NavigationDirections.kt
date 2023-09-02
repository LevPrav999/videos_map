package ru.levprav.videosmap.navigation

import androidx.navigation.NamedNavArgument

object NavigationDirections {

    val default = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = ""

        override val clearStack = false

    }

    val back = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "back"

        override val clearStack = false

    }

    val authentication = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "auth"

        override val clearStack = true

    }

    val editUser = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "edit_user"

        override val clearStack = true
    }

    val mainScreen = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "tabs_page"

        override val clearStack = true
    }
    val splash = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "splash"

        override val clearStack = true
    }
}