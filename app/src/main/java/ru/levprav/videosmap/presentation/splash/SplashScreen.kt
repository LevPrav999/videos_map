package ru.levprav.videosmap.presentation.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initAppwrite(context)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Splash")
    }
}