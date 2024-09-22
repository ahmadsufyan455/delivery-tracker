package com.zerodev.deliverytracker.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.zerodev.deliverytracker.presentation.viewmodel.LogLocationViewModel

@Composable
fun MapsScreen(
    logLocationViewModel: LogLocationViewModel
) {
    val logLocationsList = logLocationViewModel.logLocations.collectAsState().value

    if (logLocationsList.isNotEmpty()) {
        val initialPosition = LatLng(logLocationsList[0].latitude, logLocationsList[0].longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(initialPosition, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            logLocationsList.forEach { location ->
                val position = LatLng(location.latitude, location.longitude)
                Marker(
                    state = MarkerState(position = position),
                    title = "Location: ${location.latitude}, ${location.longitude}",
                    snippet = "Marker at (${location.latitude}, ${location.longitude})"
                )
            }
        }
    } else {
        Text("No log locations available")
    }
}
