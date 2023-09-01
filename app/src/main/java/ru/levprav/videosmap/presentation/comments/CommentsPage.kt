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
import androidx.compose.foundation.layout.fillMaxHeight
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
        if (viewModel.state.comments == null) {
            viewModel.loadComments(videoId)
        }
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
                            // viewModel.onClickBack()
                        }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Используем Box с весами для размещения списка и поля ввода
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.85f) // Устанавливаем вес для списка комментариев
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

            // Поле для ввода текста
            CommentUserField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f) // Устанавливаем вес для поля ввода
                    .background(Color.Red)
            )
        }
    }
}

@Composable
fun CommentItem(item: CommentModel, viewModel: CommentsViewModel) {
    val index = viewModel.state.comments?.indexOf(item) ?: -1
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Row {
            AsyncImage(
                model = viewModel.state.avatars?.get(index), contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        BorderStroke(width = 1.dp, color = Color.White), shape = CircleShape
                    )
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                item.text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(3.dp)
            )
        }
        Text(item.createdAt.toString(), style = MaterialTheme.typography.titleSmall)
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun CommentUserField(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .shadow(elevation = (0.4).dp)
    ) {
        Column(
            modifier = modifier
                .align(Alignment.BottomCenter)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
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

                    )
            }
        }
    }

}