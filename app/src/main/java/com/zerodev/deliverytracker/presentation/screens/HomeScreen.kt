package com.zerodev.deliverytracker.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.zerodev.deliverytracker.core.services.LocationService
import com.zerodev.deliverytracker.core.utils.DataStoreManager
import com.zerodev.deliverytracker.presentation.screens.components.ItemLogLocation
import com.zerodev.deliverytracker.presentation.viewmodel.LogLocationViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private val dataStore: DataStore<Preferences> = DataStoreManager.getDataStore()

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    logLocationViewModel: LogLocationViewModel = getViewModel(),
) {
    val logLocations = logLocationViewModel.getAllLogLocations().collectAsLazyPagingItems()
    var isServiceRunning by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    LaunchedEffect(Unit) {
        isServiceRunning = isServiceRunning()
    }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Log Locations: ${if (isServiceRunning) "Running" else "Stopped"}")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(count = logLocations.itemCount,
                key = logLocations.itemKey { it.id }) { index ->
                index.let {
                    val location = logLocations[it]
                    if (location != null) {
                        ItemLogLocation(logLocation = location)
                    }
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            coroutineScope.launch {
                val serviceAction = if (isServiceRunning) {
                    LocationService.ACTION_STOP
                } else {
                    LocationService.ACTION_START
                }
                Intent(applicationContext, LocationService::class.java).apply {
                    action = serviceAction
                    applicationContext.startService(this)
                }
                setServiceState(!isServiceRunning)
                isServiceRunning = !isServiceRunning
            }
        }) {
            Text(if (isServiceRunning) "Stop Service" else "Start Service")
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