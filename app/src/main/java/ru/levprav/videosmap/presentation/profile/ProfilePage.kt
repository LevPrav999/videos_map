package ru.levprav.videosmap.presentation.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfilePage(viewModel: ProfilePageViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = viewModel.state.data.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(2.dp, Color.Gray)
                .padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
        ) {
            StatisticItem(label = "Followers", count = viewModel.state.data.followersCount)
            StatisticItem(label = "Following", count = viewModel.state.data.followingCount)
            StatisticItem(label = "Likes", count = viewModel.state.data.likesCount)
        }

        viewModel.state.data.username?.let { username ->
            Text(
                text = username,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        viewModel.state.data.description?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (viewModel.state.videos!!.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(viewModel.state.videos!!.size) { item ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        AsyncImage(
                            model = viewModel.state.videos!![item].thumbnailUrl,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewModel.navigateToVideo(viewModel.state.videos!![item])
                            })
                    }
                }
            }

        } else {
            Text(text = "No videos yet.")
        }
    }
}

@Composable
fun StatisticItem(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), style = MaterialTheme.typography.headlineSmall)
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}