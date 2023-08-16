package ru.levprav.videosmap.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
fun ProfilePage(viewModel: ProfilePageViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        if(viewModel.state.isLoading){
            CircularProgressIndicator()
        }else if(viewModel.state.error != null){
            Text(viewModel.state.error!!)
        }else{
            AsyncImage(model = viewModel.state.data.imageUrl, contentDescription = null)
            viewModel.state.data.username?.let { Text(it) }
            viewModel.state.data.description?.let { Text(it) }
            Text(viewModel.state.data.followersCount.toString())
            Text(viewModel.state.data.followingCount.toString())
            Text(viewModel.state.data.likesCount.toString())
        }
    }
}