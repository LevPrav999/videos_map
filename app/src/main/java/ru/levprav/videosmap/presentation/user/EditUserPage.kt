package ru.levprav.videosmap.presentation.user

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.levprav.videosmap.R

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun EditUserPage(navController: NavController, viewModel: EditUserViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.getAvatar(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(viewModel.state.data.imageUrl) {
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
            Text(text = "Submit")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Avatar(avatarUrl: Uri?, onClick: () -> Unit) {
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    avatarUrl?.let {
        val source = ImageDecoder
            .createSource(LocalContext.current.contentResolver, it)
        bitmap.value = ImageDecoder.decodeBitmap(source)

        bitmap.value?.let { btm ->

            Image(
                bitmap = btm.asImageBitmap(),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(1.dp, Color.Black, CircleShape)
                    .padding(8.dp)
                    .clickable(onClick = onClick),
                contentScale = ContentScale.Crop
            )
        }
    } ?: Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Avatar",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .border(1.dp, Color.Black, CircleShape)
            .padding(8.dp)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Field(text: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = text) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}