package ru.levprav.videosmap.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun VideoDetailsScreen(
    uri: String,
    viewModel: VideoDetailsViewModel
) {

    var description by remember { mutableStateOf("") }
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        thumbnailBitmap = createVideoThumbnail(context, uri)?.let {
            BitmapFactory.decodeByteArray(
                createVideoThumbnail(context, uri),
                0,
                it.size
            )
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        thumbnailBitmap?.let { thumbnail ->
            Image(
                bitmap = thumbnail.asImageBitmap(),
                contentDescription = "Video Thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            )
        }

        OutlinedTextField(
            value = description,
            onValueChange = { newDescription -> description = newDescription },
            label = { Text(text = "Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                if (!viewModel.state.isLoading) {
                    viewModel.saveVideo(uri, createVideoThumbnail(context, uri)!!, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (viewModel.state.isLoading) CircularProgressIndicator(color = Color.White) else Text(
                text = "Upload video"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun createVideoThumbnail(context: Context, videoUri: String): ByteArray? {
    val retriever = MediaMetadataRetriever()

    val file = Uri.parse(videoUri)

    try {
        retriever.setDataSource(context, file)

        val thumbnailBitmap = retriever.frameAtTime
        val stream = ByteArrayOutputStream()
        thumbnailBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)

        return stream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }

    return null
}