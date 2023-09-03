package ru.levprav.videosmap.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.levprav.videosmap.navigation.AnotherProfileDestination
import ru.levprav.videosmap.navigation.CommentsNavigation
import ru.levprav.videosmap.navigation.DetailsNavigation
import ru.levprav.videosmap.navigation.NavigationDirections
import ru.levprav.videosmap.navigation.NavigationManager
import ru.levprav.videosmap.navigation.PlayerNavigation
import ru.levprav.videosmap.navigation.PreviewNavigation
import ru.levprav.videosmap.presentation.anotherProfile.AnotherProfilePage
import ru.levprav.videosmap.presentation.auth.AuthPage
import ru.levprav.videosmap.presentation.camera.VideoDetailsScreen
import ru.levprav.videosmap.presentation.camera.VideoPreviewScreen
import ru.levprav.videosmap.presentation.comments.CommentsPage
import ru.levprav.videosmap.presentation.edituser.EditUserPage
import ru.levprav.videosmap.presentation.main.TabsPage
import ru.levprav.videosmap.presentation.splash.SplashScreen
import ru.levprav.videosmap.presentation.video.VideoPlayer
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.all { it.value == true }
            if (allGranted) {
                Log.d("eee", "yes")
            } else {
                Log.d("eee", "no")
            }
        }

        setContent {

            val permissionsToRequest = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.RECORD_AUDIO
            )

            val permissionsNeeded = permissionsToRequest.filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }

            if (permissionsNeeded.isNotEmpty()) {
                requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
            }

            val navController = rememberNavController()
            LaunchedEffect(navigationManager.commands) {
                navigationManager.commands.collect { command ->
                    if (command.destination.isNotEmpty()) {
                        if (command.clearStack) {
                            navController.popBackStack()
                        }
                        if (command.destination == "back") {
                            navController.popBackStack()
                        } else {
                            navController.navigate(command.destination)
                        }
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
                        ?.let { VideoPreviewScreen(it, hiltViewModel()) }
                }

                composable(DetailsNavigation.route, arguments = DetailsNavigation.arguments)
                { backStackEntry ->

                    backStackEntry.arguments?.getString(DetailsNavigation.KEY_URI)
                        ?.let { VideoDetailsScreen(it, hiltViewModel()) }
                }

                composable(PlayerNavigation.route, arguments = PlayerNavigation.arguments)
                { backStackEntry ->

                    backStackEntry.arguments?.getString(PlayerNavigation.VIDEO_ID)?.let { it ->
                        VideoPlayer(videoId = it, hiltViewModel())
                    }
                }

                composable(PlayerNavigation.route, arguments = PlayerNavigation.arguments)
                { backStackEntry ->

                    backStackEntry.arguments?.getString(PlayerNavigation.VIDEO_ID)?.let { it ->
                        VideoPlayer(videoId = it, hiltViewModel())
                    }
                }
                composable(CommentsNavigation.route, arguments = CommentsNavigation.arguments)
                { backStackEntry ->

                    backStackEntry.arguments?.getString(CommentsNavigation.KEY)?.let { it ->
                        CommentsPage(videoId = it, hiltViewModel())
                    }
                }

                composable(
                    AnotherProfileDestination.route,
                    arguments = AnotherProfileDestination.arguments
                )
                { backStackEntry ->

                    backStackEntry.arguments?.getString(AnotherProfileDestination.KEY)?.let { it ->
                        AnotherProfilePage(userId = it, hiltViewModel())
                    }
                }
            }
        }
    }
}