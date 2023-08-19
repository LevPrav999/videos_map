package ru.levprav.videosmap.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ru.levprav.videosmap.navigation.NavigationDirections
import ru.levprav.videosmap.navigation.NavigationManager
import ru.levprav.videosmap.navigation.PreviewNavigation
import ru.levprav.videosmap.presentation.auth.AuthPage
import ru.levprav.videosmap.presentation.camera.VideoPreviewScreen
import ru.levprav.videosmap.presentation.edituser.EditUserPage
import ru.levprav.videosmap.presentation.main.TabsPage
import ru.levprav.videosmap.presentation.splash.SplashScreen
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            LaunchedEffect(navigationManager.commands) {
                navigationManager.commands.collect { command ->
                    if (command.destination.isNotEmpty()) {
                        if (command.clearStack) {
                            navController.popBackStack()
                        }
                        navController.navigate(command.destination)
                    }
                }
            }
            NavHost(
                navController = navController,
                startDestination = NavigationDirections.splash.destination
            ) {
                composable(NavigationDirections.authentication.destination) {
                    AuthPage(hiltViewModel())
                }

                composable(NavigationDirections.editUser.destination) {
                    EditUserPage(hiltViewModel())
                }
                composable(NavigationDirections.mainScreen.destination) {
                    TabsPage()
                }
                composable(NavigationDirections.splash.destination) {
                    SplashScreen(hiltViewModel())
                }
                composable(PreviewNavigation.route, arguments = PreviewNavigation.arguments)
                { backStackEntry ->

                    backStackEntry.arguments?.getString(PreviewNavigation.KEY_URI)
                        ?.let { VideoPreviewScreen(it) }
                }
            }
        }
    }
}