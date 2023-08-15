package ru.levprav.videosmap.presentation.main.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.levprav.videosmap.presentation.main.EmptyScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "profile") {
        composable("map") {
            EmptyScreen()
        }
        composable("search") {
            EmptyScreen()
        }
        composable("add") {
            EmptyScreen()
        }
        composable("notifications") {
            EmptyScreen()
        }
        composable("profile") {
            EmptyScreen()
        }
    }
}