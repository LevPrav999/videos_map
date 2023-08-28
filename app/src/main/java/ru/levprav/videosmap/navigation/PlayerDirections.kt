package ru.levprav.videosmap.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object PlayerNavigation {

    val KEY_URI = "videoId"
    val THUMBNAIL_URI = "thumbnail"
    val route = "videoDetails/{$KEY_URI}/{$THUMBNAIL_URI}"
    val arguments = listOf(
        navArgument(KEY_URI) { type = NavType.StringType },
        navArgument(THUMBNAIL_URI) { type = NavType.StringType }
    )

    fun playerScreen(
        videoUrl: String? = null,
        thumbnailUrl: String? = null
    ) = object : NavigationCommand {


        override val destination = "videoDetails/$videoUrl/$thumbnailUrl"
        override val arguments = listOf(
            navArgument(KEY_URI) { type = NavType.StringType },
            navArgument(THUMBNAIL_URI) { type = NavType.StringType }
        )
        override val clearStack = false
    }
}