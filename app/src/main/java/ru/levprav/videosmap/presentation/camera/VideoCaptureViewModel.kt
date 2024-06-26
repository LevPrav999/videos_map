package ru.levprav.videosmap.presentation.camera

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.levprav.videosmap.navigation.NavigationManager
import ru.levprav.videosmap.navigation.PreviewNavigation
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class VideoCaptureScreenViewModel @Inject constructor(
    private val navigationManager: NavigationManager
) : ViewModel() {
    fun navigate(uri: String) {
        val uriEncoded = URLEncoder.encode(
            uri,
            StandardCharsets.UTF_8.toString()
        )
        navigationManager.navigate(PreviewNavigation.previewScreen(uriEncoded))
    }
}