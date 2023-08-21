package ru.levprav.videosmap.presentation.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream


@Composable
fun VideoDetailsScreen(
    uri: String,
    viewModel: VideoDetailsViewModel
) {
    var description by remember { mutableStateOf("") }
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit){
        thumbnailBitmap = createVideoThumbnail(uri)?.let { BitmapFactory.decodeByteArray(createVideoThumbnail(uri), 0, it.size) }
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
                viewModel.saveVideo(uri, createVideoThumbnail(uri)!!)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Upload video")
        }
    }
}

fun createVideoThumbnail(videoUri: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    try {
        retriever.setDataSource(videoUri)
        val thumbnailBitmap = retriever.frameAtTime
        val outputStream = ByteArrayOutputStream()
        thumbnailBitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }
    return null
}