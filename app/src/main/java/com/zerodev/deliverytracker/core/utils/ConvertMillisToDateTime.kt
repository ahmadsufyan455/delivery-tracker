package com.zerodev.deliverytracker.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertMillisToDateTime(millis: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = Date(millis)
    return sdf.format(date)
}