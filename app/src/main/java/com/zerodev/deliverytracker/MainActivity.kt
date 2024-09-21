package com.zerodev.deliverytracker

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.asFlow
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.zerodev.deliverytracker.core.services.LocationService
import com.zerodev.deliverytracker.presentation.viewmodel.LogLocationViewModel
import com.zerodev.deliverytracker.ui.theme.DeliveryTrackerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val logLocationViewModel: LogLocationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS,
                ), 0
            )
        }
        setContent {
            DeliveryTrackerTheme {
                val logLocations =
                    logLocationViewModel.getAllLogLocations().asFlow().collectAsLazyPagingItems()

                Column(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(onClick = {
                            Intent(applicationContext, LocationService::class.java).apply {
                                action = LocationService.ACTION_START
                                startService(this)
                            }
                        }) {
                            Text(text = "Start")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            Intent(applicationContext, LocationService::class.java).apply {
                                action = LocationService.ACTION_STOP
                                startService(this)
                            }
                        }) {
                            Text(text = "Stop")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Log Locations:")
                        LazyColumn {
                            items(count = logLocations.itemCount,
                                key = logLocations.itemKey { it.id }) { index ->
                                index.let {
                                    val location = logLocations[it]
                                    Text("Location: (${location?.latitude}, ${location?.longitude})")
                                }
                            }

                            // Handle loading and error states
                            when {
                                logLocations.loadState.refresh is LoadState.Loading -> {
                                    item { CircularProgressIndicator() }
                                }

                                logLocations.loadState.append is LoadState.Loading -> {
                                    item { CircularProgressIndicator() }
                                }

                                logLocations.loadState.refresh is LoadState.Error -> {
                                    val e = logLocations.loadState.refresh as LoadState.Error
                                    item { Text("Error: ${e.error.localizedMessage}") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}