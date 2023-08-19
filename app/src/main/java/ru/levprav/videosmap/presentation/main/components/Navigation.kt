package ru.levprav.videosmap.presentation.main.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.levprav.videosmap.navigation.TabsDirections
import ru.levprav.videosmap.presentation.main.EmptyScreen
import ru.levprav.videosmap.presentation.profile.ProfilePage

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = TabsDirections.profile.destination) {
        composable(TabsDirections.map.destination) {
            EmptyScreen()
        }
        composable(TabsDirections.search.destination) {
            EmptyScreen()
        }
        composable(TabsDirections.add.destination) {
            EmptyScreen()
        }
        composable(TabsDirections.notifications.destination) {
            EmptyScreen()
        }
        composable(TabsDirections.profile.destination) {
            ProfilePage(hiltViewModel())
        }
    }
}