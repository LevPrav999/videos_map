package ru.levprav.videosmap.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object DetailsNavigation {

    val KEY_URI = "videoUri"
    val route = "videoDetails/{$KEY_URI}"
    val arguments = listOf(
        navArgument(KEY_URI) { type = NavType.StringType }
    )

    fun detailsScreen(
        uri: String? = null
    ) = object : NavigationCommand {


        override val destination = "videoDetails/$uri"
        override val arguments = listOf(
            navArgument(KEY_URI) { type = NavType.StringType }
        )
        override val clearStack = false
    }
}