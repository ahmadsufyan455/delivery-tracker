package com.zerodev.deliverytracker.core.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.zerodev.deliverytracker.R
import com.zerodev.deliverytracker.domain.repositories.LogLocationRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext

class LogWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private val logLocationRepository: LogLocationRepository = GlobalContext.get().get()

    override fun doWork(): Result {
        runBlocking {
            logLocationRepository.deleteAllLogLocations()
        }
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_WORKER)
            .setContentTitle("Data removed successfully...")
            .setContentText("Your log location data has been removed")
            .setSmallIcon(R.drawable.baseline_remove)
            .build()
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_WORKER, notification)
    }

    companion object {
        private const val NOTIFICATION_ID_WORKER = 2
        const val CHANNEL_ID_WORKER = "channel_02"
        const val CHANNEL_NAME_WORKER = "worker channel"
    }
}