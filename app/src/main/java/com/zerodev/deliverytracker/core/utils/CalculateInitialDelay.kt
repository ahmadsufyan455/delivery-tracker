package com.zerodev.deliverytracker.core.utils

import java.util.Calendar

fun calculateInitialDelay(hour: Int, minute: Int): Long {
    val currentDateTime = Calendar.getInstance()
    val targetDateTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }
    if (targetDateTime.before(currentDateTime)) {
        targetDateTime.add(Calendar.DAY_OF_YEAR, 1)
    }
    val initialDelay = targetDateTime.timeInMillis - currentDateTime.timeInMillis
    return initialDelay
}