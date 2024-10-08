package com.zerodev.deliverytracker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zerodev.deliverytracker.presentation.screens.HomeScreen
import com.zerodev.deliverytracker.presentation.screens.MapsScreen
import com.zerodev.deliverytracker.presentation.viewmodel.LogLocationViewModel
import com.zerodev.deliverytracker.ui.theme.DeliveryTrackerTheme
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        val logLocationViewModel: LogLocationViewModel = getViewModel()

                        NavHost(navController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(
                                    navController = navController,
                                    logLocationViewModel = logLocationViewModel
                                )
                            }
                            composable("maps") { MapsScreen(logLocationViewModel = logLocationViewModel) }
                        }
                    }
                }
            }
        }
    }
}