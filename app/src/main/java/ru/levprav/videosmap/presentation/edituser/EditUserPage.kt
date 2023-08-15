package ru.levprav.videosmap.presentation.edituser

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.levprav.videosmap.presentation.edituser.components.Avatar
import ru.levprav.videosmap.presentation.edituser.components.Field


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun EditUserPage(viewModel: EditUserViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.getAvatar(it)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.state) {
        viewModel.state.error?.let { error ->
            if (!viewModel.state.isLoading) {
                snackbarHostState.showSnackbar(error)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(
            viewModel.state.data.imageUrl,
            viewModel.state.data.imageUrlNetwork,
            viewModel.state.isFromNetwork
        ) {
            launcher.launch("image/*")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Field("Description", viewModel.state.data.description ?: "") {
            viewModel.onDescriptionChange(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Field("Username", viewModel.state.data.username ?: "") {
            viewModel.onUsernameChange(it)
        }
        Button(
            onClick = { viewModel.submit() },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            if (viewModel.state.isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(text = "Submit")
            }
        }
    }
}