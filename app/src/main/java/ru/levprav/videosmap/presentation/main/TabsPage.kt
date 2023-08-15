package ru.levprav.videosmap.presentation.main

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.levprav.videosmap.presentation.main.components.BottomNavigationBar
import ru.levprav.videosmap.presentation.main.components.Navigation


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TabsPage() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "Map",
                        route = "map",
                        icon = Icons.Rounded.Home
                    ),
                    BottomNavItem(
                        name = "Search",
                        route = "search",
                        icon = Icons.Default.Search
                    ),
                    BottomNavItem(
                        name = "Add",
                        route = "add",
                        icon = Icons.Default.Add
                    ),
                    BottomNavItem(
                        name = "Notifications",
                        route = "notifications",
                        icon = Icons.Default.Notifications
                    ),
                    BottomNavItem(
                        name = "Profile",
                        route = "profile",
                        icon = Icons.Default.Person
                    ),
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) {
        Navigation(navController = navController)
    }
}
