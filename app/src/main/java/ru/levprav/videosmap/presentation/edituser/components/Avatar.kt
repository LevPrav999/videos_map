package ru.levprav.videosmap.presentation.edituser.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.levprav.videosmap.R

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Avatar(avatarUri: Uri?, imageUrlNetwork: String?, isFromNetwork: Boolean, onClick: () -> Unit) {
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    if (!isFromNetwork) {
        avatarUri?.let {
            val source = ImageDecoder
                .createSource(LocalContext.current.contentResolver, avatarUri)
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
    } else {
        AsyncImage(
            model = imageUrlNetwork,
            contentDescription = null,
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

}