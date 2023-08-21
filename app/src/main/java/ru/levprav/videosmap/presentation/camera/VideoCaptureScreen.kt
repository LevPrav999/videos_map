package ru.levprav.videosmap.presentation.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import ru.levprav.videosmap.R
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun VideoCaptureScreen(viewModel: VideoCaptureScreenViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    val recording = remember { mutableStateOf<Recording?>(null) }
    val previewView: PreviewView = remember { PreviewView(context) }
    val videoCapture: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }
    val recordingStarted: MutableState<Boolean> = remember { mutableStateOf(false) }

    val audioEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
    val cameraSelector: MutableState<CameraSelector> = remember {
        mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(previewView) {
        videoCapture.value = createVideoCaptureUseCase(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = cameraSelector.value,
            previewView = previewView,
            context = context
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = {
                if (!recordingStarted.value) {
                    videoCapture.value?.let { videoCapture ->
                        recordingStarted.value = true
                        val mediaDir = context.externalCacheDirs.firstOrNull()?.let {
                            File(it, context.getString(R.string.app_name)).apply { mkdirs() }
                        }

                        recording.value = startRecordingVideo(
                            context = context,
                            filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                            videoCapture = videoCapture,
                            outputDirectory = if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir,
                            executor = context.mainExecutor,
                            audioEnabled = audioEnabled.value
                        ) { event ->
                            if (event is VideoRecordEvent.Finalize) {
                                val uri = event.outputResults.outputUri
                                if (uri != Uri.EMPTY) {
                                    viewModel.navigate(uri.toString())
                                }
                            }
                        }
                    }
                } else {
                    recordingStarted.value = false
                    recording.value?.stop()
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 32.dp)
        ) {
            Icon(
                painter = painterResource(if (recordingStarted.value) R.drawable.ic_stop else R.drawable.ic_record),
                contentDescription = "",
                modifier = Modifier.size(64.dp),
                tint = Color.White
            )
        }
        if (!recordingStarted.value) {
            IconButton(
                onClick = {
                    audioEnabled.value = !audioEnabled.value
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(bottom = 32.dp)
            ) {
                Icon(
                    painter = painterResource(if (audioEnabled.value) R.drawable.ic_mic_on else R.drawable.ic_mic_off),
                    contentDescription = "",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
            }
        }
        if (!recordingStarted.value) {
            IconButton(
                onClick = {
                    cameraSelector.value =
                        if (cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                        else CameraSelector.DEFAULT_BACK_CAMERA
                    lifecycleOwner.lifecycleScope.launch {
                        videoCapture.value = createVideoCaptureUseCase(
                            lifecycleOwner = lifecycleOwner,
                            cameraSelector = cameraSelector.value,
                            previewView = previewView,
                            context = context
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(bottom = 32.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cameraswitch),
                    contentDescription = "",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
            }
        }
    }
}

fun startRecordingVideo(
    context: Context,
    filenameFormat: String,
    videoCapture: VideoCapture<Recorder>,
    outputDirectory: File,
    executor: Executor,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording? {
    val videoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.ROOT).format(System.currentTimeMillis()) + ".mp4"
    )

    val outputOptions = FileOutputOptions.Builder(videoFile).build()

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return videoCapture.output
            .prepareRecording(context, outputOptions)
            .apply { if (audioEnabled) withAudioEnabled() }
            .start(executor, consumer)
    }
    return null
}

@RequiresApi(Build.VERSION_CODES.P)
suspend fun createVideoCaptureUseCase(
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    previewView: PreviewView,
    context: Context
): VideoCapture<Recorder> {
    val preview = Preview.Builder()
        .build()
        .apply { setSurfaceProvider(previewView.surfaceProvider) }

    val qualitySelector = QualitySelector.from(
        Quality.HD,
        FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
    )
    val recorder = Recorder.Builder()
        .setExecutor(context.mainExecutor)
        .setQualitySelector(qualitySelector)
        .build()
    val videoCapture = VideoCapture.withOutput(recorder)

    val cameraProvider = getCameraProvider(context)
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        preview,
        videoCapture
    )

    return videoCapture
}

@RequiresApi(Build.VERSION_CODES.P)
suspend fun getCameraProvider(context: Context): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(context).also { future ->
            future.addListener(
                {
                    continuation.resume(future.get())
                },
                context.mainExecutor
            )
        }
    }