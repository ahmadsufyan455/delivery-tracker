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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.zerodev.deliverytracker.core.services.LocationService
import com.zerodev.deliverytracker.core.utils.DataStoreManager
import com.zerodev.deliverytracker.presentation.viewmodel.LogLocationViewModel
import com.zerodev.deliverytracker.ui.theme.DeliveryTrackerTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val logLocationViewModel: LogLocationViewModel by viewModel()
    private val dataStore: DataStore<Preferences> = DataStoreManager.getDataStore()

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
                val logLocations = logLocationViewModel.getAllLogLocations().asFlow().collectAsLazyPagingItems()
                var isServiceRunning by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    isServiceRunning = isServiceRunning()
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(onClick = {
                            lifecycleScope.launch {
                                val serviceAction = if (isServiceRunning) {
                                    LocationService.ACTION_STOP
                                } else {
                                    LocationService.ACTION_START
                                }

                                Intent(applicationContext, LocationService::class.java).apply {
                                    action = serviceAction
                                    startService(this)
                                }
                                setServiceState(!isServiceRunning)
                                isServiceRunning = !isServiceRunning
                            }
                        }) {
                            Text(if (isServiceRunning) "Stop Service" else "Start Service")
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

    private suspend fun isServiceRunning(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[booleanPreferencesKey("isServiceRunning")] ?: false
    }

    private suspend fun setServiceState(isRunning: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("isServiceRunning")] = isRunning
        }
    }
}