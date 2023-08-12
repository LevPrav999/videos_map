package ru.levprav.videosmap.presentation.main

import android.text.Layout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EmptyScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Empty")
    }
}