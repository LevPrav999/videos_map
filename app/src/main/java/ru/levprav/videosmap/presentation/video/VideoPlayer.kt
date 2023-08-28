package ru.levprav.videosmap.presentation.video
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import ru.levprav.videosmap.R

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    video: String,
    thumbnailUrl: String,
) {
    val context = LocalContext.current
    var thumbnail by remember {
        mutableStateOf(Pair(thumbnailUrl, true))  //url, isShow
    }
    var pauseButtonVisibility by remember { mutableStateOf(false) }
    var isFirstFrameLoad = remember { false }

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            repeatMode = Player.REPEAT_MODE_ONE
            setMediaItem(MediaItem.fromUri(Uri.parse(video)))
            playWhenReady = true
            prepare()
            addListener(object : Player.Listener {
                override fun onRenderedFirstFrame() {
                    super.onRenderedFirstFrame()
                    isFirstFrameLoad = true
                    thumbnail = thumbnail.copy(second = false)
                }
            })
        }
    }

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(key1 = lifecycleOwner) {
        val lifeCycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    exoPlayer.pause()
                    pauseButtonVisibility = false
                }
                Lifecycle.Event.ON_START -> exoPlayer.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifeCycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifeCycleObserver)
        }
    }

    val playerView = remember {
        PlayerView(context).apply {
            player = exoPlayer
            useController = false
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    DisposableEffect(key1 = AndroidView(factory = {
        playerView
    }, modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(onTap = {
            if(exoPlayer.isPlaying){
                exoPlayer.pause()
            }else{
                exoPlayer.play()
            }
            pauseButtonVisibility = !pauseButtonVisibility
        }, onDoubleTap = { offset ->

        })
    }), effect = {
        onDispose {
            thumbnail = thumbnail.copy(second = true)
            exoPlayer.release()
            pauseButtonVisibility = false
        }
    })

    AndroidView(factory = {
        playerView
    }, modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(onTap = {
            if(exoPlayer.isPlaying){
                exoPlayer.pause()
            }else{
                exoPlayer.play()
            }
            pauseButtonVisibility = !pauseButtonVisibility
        }, onDoubleTap = { offset ->
            // like
        })
    })

    AnimatedVisibility(
        visible = pauseButtonVisibility,
        enter = scaleIn(spring(Spring.DampingRatioMediumBouncy), initialScale = 1.5f),
        exit = scaleOut(tween(150))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(36.dp)
        )
    }

    if (thumbnail.second) {
        AsyncImage(
            model = thumbnail,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

}