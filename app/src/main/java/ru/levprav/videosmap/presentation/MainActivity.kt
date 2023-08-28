package ru.levprav.videosmap.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
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
import ru.levprav.videosmap.navigation.DetailsNavigation
import ru.levprav.videosmap.navigation.NavigationDirections
import ru.levprav.videosmap.navigation.NavigationManager
import ru.levprav.videosmap.navigation.PlayerNavigation
import ru.levprav.videosmap.navigation.PreviewNavigation
import ru.levprav.videosmap.presentation.auth.AuthPage
import ru.levprav.videosmap.presentation.camera.VideoDetailsScreen
import ru.levprav.videosmap.presentation.camera.VideoPreviewScreen
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
                Manifest.permission.READ_MEDIA_VIDEO,
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
                        ?.let { VideoPreviewScreen(it, hiltViewModel()) }
                }

                composable(DetailsNavigation.route, arguments = DetailsNavigation.arguments)
                { backStackEntry ->

                    backStackEntry.arguments?.getString(DetailsNavigation.KEY_URI)
                        ?.let { VideoDetailsScreen(it, hiltViewModel()) }
                }

                composable(PlayerNavigation.route, arguments = PlayerNavigation.arguments)
                { backStackEntry ->

                    backStackEntry.arguments?.getString(PlayerNavigation.KEY_URI)
                        ?.let { it1 -> backStackEntry.arguments?.getString(PlayerNavigation.THUMBNAIL_URI)?.let { it2 ->
                            VideoPlayer(video = it1, thumbnailUrl = it2)
                        } }
                }
            }
        }
    }
}