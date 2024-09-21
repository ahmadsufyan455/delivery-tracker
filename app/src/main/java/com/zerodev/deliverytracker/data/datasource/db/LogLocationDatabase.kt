package com.zerodev.deliverytracker.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zerodev.deliverytracker.data.model.LogLocation

@Database(entities = [LogLocation::class], version = 1, exportSchema = false)
abstract class LogLocationDatabase : RoomDatabase() {
    abstract fun logLocationDao(): LogLocationDao
}