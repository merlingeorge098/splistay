package com.example.splistay.ui.screens.maps

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryFinder(
    onBack: () -> Unit
) {
    val pgLocation = LatLng(12.9716, 77.5946) // Placeholder: Bangalore
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pgLocation, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grocery Finder") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = rememberMarkerState(position = pgLocation),
                    title = "Your PG",
                    snippet = "Sunny Meadows"
                )
                
                // Mock grocery stores
                Marker(
                    state = rememberMarkerState(position = LatLng(12.9720, 77.5950)),
                    title = "Fresh Mart",
                    snippet = "Grocery Store"
                )
                Marker(
                    state = rememberMarkerState(position = LatLng(12.9710, 77.5940)),
                    title = "Daily Needs",
                    snippet = "General Store"
                )
            }
        }
    }
}
