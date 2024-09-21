package com.zerodev.deliverytracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.zerodev.deliverytracker.core.services.LocationService.Companion.CHANNEL_ID_SERVICE
import com.zerodev.deliverytracker.core.services.LocationService.Companion.CHANNEL_NAME_SERVICE

class DeliveryTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelService = NotificationChannel(
                CHANNEL_ID_SERVICE,
                CHANNEL_NAME_SERVICE,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManagerService = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManagerService.createNotificationChannel(channelService)
        }
    }
}