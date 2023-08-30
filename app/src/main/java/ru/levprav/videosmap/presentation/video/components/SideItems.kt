package ru.levprav.videosmap.presentation.video.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.levprav.videosmap.R
import ru.levprav.videosmap.domain.models.VideoModel
import ru.levprav.videosmap.presentation.video.VideoPlayerViewModel
import ru.levprav.videosmap.utils.IntentUtils.share

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