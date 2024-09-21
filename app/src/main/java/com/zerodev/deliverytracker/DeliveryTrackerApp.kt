package com.zerodev.deliverytracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.zerodev.deliverytracker.core.services.LocationService.Companion.CHANNEL_ID_SERVICE
import com.zerodev.deliverytracker.core.services.LocationService.Companion.CHANNEL_NAME_SERVICE
import com.zerodev.deliverytracker.core.services.LogWorker
import com.zerodev.deliverytracker.core.services.LogWorker.Companion.CHANNEL_ID_WORKER
import com.zerodev.deliverytracker.core.services.LogWorker.Companion.CHANNEL_NAME_WORKER
import com.zerodev.deliverytracker.core.utils.DataStoreManager
import com.zerodev.deliverytracker.core.utils.calculateInitialDelay
import com.zerodev.deliverytracker.di.databaseModule
import com.zerodev.deliverytracker.di.repositoryModule
import com.zerodev.deliverytracker.di.useCaseModule
import com.zerodev.deliverytracker.di.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.concurrent.TimeUnit

class DeliveryTrackerApp : Application() {
    private lateinit var workManager: WorkManager
    override fun onCreate() {
        super.onCreate()
        DataStoreManager.initialize(this)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelService = NotificationChannel(
                CHANNEL_ID_SERVICE,
                CHANNEL_NAME_SERVICE,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManagerService = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManagerService.createNotificationChannel(channelService)

            val channelWorker = NotificationChannel(
                CHANNEL_ID_WORKER,
                CHANNEL_NAME_WORKER,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManagerWorker = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManagerWorker.createNotificationChannel(channelWorker)
        }

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@DeliveryTrackerApp)
            modules(
                listOf(
                    databaseModule,
                    repositoryModule,
                    useCaseModule,
                    viewmodelModule
                )
            )
        }

        workManager = WorkManager.getInstance(this)
        removeDataPeriodic()
    }

    private fun removeDataPeriodic() {
        val removeData = PeriodicWorkRequestBuilder<LogWorker>(15, TimeUnit.MINUTES)
            .setInitialDelay(calculateInitialDelay(22, 0), TimeUnit.HOURS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "DataCleanUp",
            ExistingPeriodicWorkPolicy.UPDATE,
            removeData
        )
    }
}