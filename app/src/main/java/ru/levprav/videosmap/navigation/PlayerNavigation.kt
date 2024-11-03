package ru.levprav.videosmap.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object PlayerNavigation {

    val VIDEO_ID = "videoId"
    val route = "video/{$VIDEO_ID}"
    val arguments = listOf(
        navArgument(VIDEO_ID) { type = NavType.StringType },
    )

    fun playerScreen(
        videoId: String? = null,
    ) = object : NavigationCommand {


        override val destination = "video/$videoId"
        override val arguments = listOf(
            navArgument(VIDEO_ID) { type = NavType.StringType },
        )
        override val clearStack = false
    }
}