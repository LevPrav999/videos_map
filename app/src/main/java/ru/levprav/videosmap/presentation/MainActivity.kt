package ru.levprav.videosmap.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.levprav.videosmap.presentation.auth.AuthPage
import ru.levprav.videosmap.presentation.main.TabsPage
import ru.levprav.videosmap.presentation.user.EditUserPage


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "tabs_page") {
                composable("auth") {
                    AuthPage(navController = navController, hiltViewModel())
                }

                composable("edit_user") {
                    EditUserPage(navController = navController, hiltViewModel())
                }
                composable("tabs_page") {
                    TabsPage()
                }
            }
        }
    }
}