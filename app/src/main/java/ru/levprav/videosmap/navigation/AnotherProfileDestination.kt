package ru.levprav.videosmap.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object AnotherProfileDestination {
    val KEY = "userId"
    val route = "anotherProfile/{$KEY}"
    val arguments = listOf(
        navArgument(KEY) { type = NavType.StringType }
    )

    fun anotherProfileScreen(
        userId: String? = null
    ) = object : NavigationCommand {


        override val destination = "anotherProfile/$userId"
        override val arguments = listOf(
            navArgument(KEY) { type = NavType.StringType }
        )
        override val clearStack = false
    }
}