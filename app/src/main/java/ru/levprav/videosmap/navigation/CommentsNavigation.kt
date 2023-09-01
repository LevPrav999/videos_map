package ru.levprav.videosmap.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object CommentsNavigation {

    val KEY = "videoId"
    val route = "comments/{$KEY}"
    val arguments = listOf(
        navArgument(KEY) { type = NavType.StringType }
    )

    fun commentsScreen(
        videoId: String? = null
    ) = object : NavigationCommand {


        override val destination = "comments/$videoId"
        override val arguments = listOf(
            navArgument(KEY) { type = NavType.StringType }
        )
        override val clearStack = false
    }
}