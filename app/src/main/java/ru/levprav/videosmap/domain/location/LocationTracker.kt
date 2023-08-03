package ru.levprav.videosmap.domain.location

import com.google.android.gms.maps.model.LatLng

interface LocationTracker {
    suspend fun getCurrentLocation(): LatLng?
}