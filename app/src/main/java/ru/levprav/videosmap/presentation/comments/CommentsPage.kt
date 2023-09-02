package ru.levprav.videosmap.presentation.comments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.levprav.videosmap.R
import ru.levprav.videosmap.domain.models.CommentModel

@Composable
fun CommentsPage(
    videoId: String,
    viewModel: CommentsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadComments(videoId)
    }
    if (viewModel.state.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "${viewModel.state.comments?.size ?: ""} Comments",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clickable {
                            viewModel.popBack()
                        }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Используем Column с весами для размещения списка и поля ввода
            Column(
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(top = 4.dp),
                ) {
                    viewModel.state.comments?.let {
                        items(it) { comment ->
                            CommentItem(comment, viewModel)
                        }
                    }
                }
            }

            // CommentUserField всегда внизу экрана
            CommentUserField(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel,
                videoId = videoId
            )
        }
    }
}

@Composable
fun CommentItem(item: CommentModel, viewModel: CommentsViewModel) {
    val index = viewModel.state.comments?.indexOf(item) ?: -1
    if (index < viewModel.state.users.size) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Row {
                AsyncImage(
                    model = viewModel.state.users[index].imageUrl, contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            BorderStroke(width = 1.dp, color = Color.White), shape = CircleShape
                        )
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    viewModel.state.users[index].name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(3.dp)
                )
            }
            Text(item.text, style = MaterialTheme.typography.titleSmall)
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

}

@Composable
fun CommentUserField(modifier: Modifier, viewModel: CommentsViewModel, videoId: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = (0.4).dp)
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = viewModel.state.commentValue,
                onValueChange = {
                    viewModel.onValueChanged(it)
                },
                shape = RoundedCornerShape(36.dp),
                placeholder = {
                    Text(text = "Write here")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Gray,
                    unfocusedContainerColor = Color.Gray,
                    disabledContainerColor = Color.Gray,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .border(
                        BorderStroke(width = 1.dp, color = Color.White), shape = CircleShape
                    )
                    .clip(shape = CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        viewModel.postComment(videoId = videoId)
                    }
                    .padding(10.dp)
            )
        }
    }
}