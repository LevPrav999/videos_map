package ru.levprav.videosmap.presentation.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import ru.levprav.videosmap.R

@Composable
fun VideoPreviewScreen(
    uri: String,
    viewModel: VideoPreviewViewModel
) {
    val context = LocalContext.current

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri.trim('{', '}')))
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                StyledPlayerView(context).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        IconButton(onClick = {
            viewModel.navigate(uri.trim('{', '}'))
        }, modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(bottom = 32.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_next_arrow),
                contentDescription = "",
                modifier = Modifier.size(64.dp),
                tint = Color.White
            )
        }
    }
}