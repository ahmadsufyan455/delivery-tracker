package com.zerodev.deliverytracker.core.utils

import java.util.Calendar
import java.util.concurrent.TimeUnit

fun calculateInitialDelay(): Long {
    val now = System.currentTimeMillis()
    var targetTime = calculateTargetTime()
    return if (targetTime > now) {
        targetTime - now
    } else {
        val tomorrow = now + TimeUnit.DAYS.toMillis(1)
        targetTime = calculateTargetTime(tomorrow)
        targetTime - now
    }
}

fun calculateTargetTime(timeInMillis: Long = System.currentTimeMillis()): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    calendar.set(Calendar.HOUR_OF_DAY, 22) // Set hour to 10 PM
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    return calendar.timeInMillis
}