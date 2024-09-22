package com.zerodev.deliverytracker.core.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.zerodev.deliverytracker.R
import com.zerodev.deliverytracker.core.utils.isOnline
import com.zerodev.deliverytracker.data.model.LogLocation
import com.zerodev.deliverytracker.domain.usecases.InsertLocationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private val insetLogLocationUseCase: InsertLocationUseCase by inject()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun insertLogLocation(latitude: Double, longitude: Double) {
        val logLocation = LogLocation(
            latitude = latitude,
            longitude = longitude,
            timestamp = System.currentTimeMillis(),
            isOnline = isOnline(applicationContext)
        )
        serviceScope.launch {
            insetLogLocationUseCase.invoke(logLocation)
        }
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_SERVICE)
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.baseline_location)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        locationClient
            .getLocationUpdates(300000L) // interval every 5 minutes
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                insertLogLocation(lat.toDouble(), long.toDouble())
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(NOTIFICATION_ID_SERVICE, updatedNotification.build())
            }
            .launchIn(serviceScope)
        startForeground(NOTIFICATION_ID_SERVICE, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        private const val NOTIFICATION_ID_SERVICE = 1
        const val CHANNEL_ID_SERVICE = "channel_01"
        const val CHANNEL_NAME_SERVICE = "location channel"
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}