package ru.levprav.videosmap.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object PreviewNavigation {

    val KEY_URI = "uri"
    val route = "preview/{$KEY_URI}"
    val arguments = listOf(
        navArgument(KEY_URI) { type = NavType.StringType }
    )

    fun previewScreen(
        uri: String? = null
    ) = object : NavigationCommand {


        override val destination = "preview/$uri"
        override val arguments = listOf(
            navArgument(KEY_URI) { type = NavType.StringType }
        )
        override val clearStack = false
    }
}