package ru.levprav.videosmap.presentation.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun SplashScreen(viewModel: SplashViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Splash")
    }
}