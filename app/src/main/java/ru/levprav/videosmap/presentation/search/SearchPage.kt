package ru.levprav.videosmap.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SearchPage(viewModel: SearchViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        TextField(viewModel)

        Spacer(modifier = Modifier.width(10.dp))

        if (viewModel.state.isLoading) {
            CircularProgressIndicator()
        } else if (viewModel.state.data.isNotEmpty()) {
            VideosList(viewModel)
        } else {
            Text(text = "No videos found. Search function working only with full words.")
        }
    }
}

@Composable
fun TextField(viewModel: SearchViewModel) {
    OutlinedTextField(
        value = viewModel.state.textFieldValue,
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
        )
    )
}

@Composable
fun VideosList(viewModel: SearchViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(viewModel.state.data.size) { item ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = viewModel.state.data[item].thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        viewModel.onVideoClicked(viewModel.state.data[item].id)
                    })
            }
        }
    }
}