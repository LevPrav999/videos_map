package ru.levprav.videosmap.presentation.video

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.utils.IntentUtils.share

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    videoId: String,
    viewModel: VideoPlayerViewModel
) {
    if (viewModel.state.data == null) {
        viewModel.loadVideoModel(videoId)
        CircularProgressIndicator()
    } else {
        val video = viewModel.state.data!!
        val context = LocalContext.current
        var thumbnail by remember {
            mutableStateOf(Pair(video.thumbnailUrl, true))  //url, isShow
        }
        var pauseButtonVisibility by remember { mutableStateOf(false) }
        var isFirstFrameLoad = remember { false }

        val exoPlayer = remember(context) {
            ExoPlayer.Builder(context).build().apply {
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_ONE
                setMediaItem(MediaItem.fromUri(Uri.parse(video.url)))
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


        DisposableEffect(Unit) {
            onDispose {
                thumbnail = thumbnail.copy(second = true)
                exoPlayer.release()
                pauseButtonVisibility = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = {
                playerView
            }, modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = {
                    pauseButtonVisibility = if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                        true
                    } else {
                        exoPlayer.play()
                        false
                    }
                }, onDoubleTap = { offset ->
                    // like
                })
            })

            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    FooterUi(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        item = video
                    )

                    SideItems(
                        modifier = Modifier,
                        item = video,
                        onclickComment = {},
                        onClickUser = {
                            //viewModel.navigateToUser()
                        },
                        onClickShare = {
                            context.share(
                                text = "https://github.com/LevPrav999"
                            )
                        },
                        viewModel = viewModel
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            AnimatedVisibility(
                visible = pauseButtonVisibility,
                enter = scaleIn(spring(Spring.DampingRatioMediumBouncy), initialScale = 1.5f),
                exit = scaleOut(tween(150)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp),
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

            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(5.dp)
                    .clickable {
                        viewModel.navigateBack()
                    },
            )
        }
    }

}

@Composable
fun SideItems(
    modifier: Modifier,
    item: VideoModel,
    onclickComment: (videoId: String) -> Unit,
    onClickUser: (userId: String) -> Unit,
    onClickShare: (() -> Unit)? = null,
    viewModel: VideoPlayerViewModel
) {

    val context = LocalContext.current
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        viewModel.state.avatar?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        BorderStroke(width = 1.dp, color = Color.White), shape = CircleShape
                    )
                    .clip(shape = CircleShape)
                    .clickable {
                        onClickUser.invoke(item.userId)
                    },
                contentScale = ContentScale.Crop
            )
        } ?: Image(
            painter =
            painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(50.dp)
                .border(
                    BorderStroke(width = 1.dp, color = Color.White), shape = CircleShape
                )
                .clip(shape = CircleShape)
                .clickable {
                    onClickUser.invoke(item.userId)
                },
            contentScale = ContentScale.Crop
        )

        if (!viewModel.state.isSubscribed) {
            Image(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = null,
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(5.5.dp)
                    .clickable {
                        viewModel.follow(item.userId)
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Spacer(modifier = modifier.height(12.dp))


        LikeIconButton(
            modifier = modifier,
            isLiked = viewModel.state.isLiked,
            likeCount = item.liked.size.toString(),
            onLikedClicked = {
                if (viewModel.state.isLiked) viewModel.unlike(item.id) else viewModel.like(item.id)
            })


        Icon(painter = painterResource(id = R.drawable.ic_comment),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(33.dp)
                .clickable {
                    onclickComment(item.id)
                })

        Spacer(modifier = modifier.height(14.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    onClickShare?.let { onClickShare.invoke() } ?: run {
                        context.share(
                            text = "https://github.com/LevPrav999"
                        )
                    }
                }
        )

        Spacer(modifier = modifier.height(40.dp))

    }
}

@Composable
fun LikeIconButton(
    modifier: Modifier, isLiked: Boolean, likeCount: String, onLikedClicked: (Boolean) -> Unit
) {

    val maxSize = 38.dp
    val iconSize by animateDpAsState(targetValue = if (isLiked) 33.dp else 32.dp,
        animationSpec = keyframes {
            durationMillis = 400
            24.dp.at(50)
            maxSize.at(190)
            26.dp.at(330)
            32.dp.at(400).with(FastOutLinearInEasing)
        })

    Box(
        modifier = Modifier
            .size(maxSize)
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onLikedClicked(!isLiked)
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = null,
            tint = if (isLiked) MaterialTheme.colorScheme.primary else Color.White,
            modifier = Modifier.size(iconSize)
        )
    }

    Text(text = likeCount, style = MaterialTheme.typography.labelMedium)
    Spacer(modifier = modifier.height(16.dp))
}

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


