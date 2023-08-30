package ru.levprav.videosmap.presentation.video.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.levprav.videosmap.domain.models.VideoModel

@Composable
fun FooterUi(
    modifier: Modifier,
    item: VideoModel
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Bottom) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

    }
}