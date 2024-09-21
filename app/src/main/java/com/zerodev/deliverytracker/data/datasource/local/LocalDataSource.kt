package com.zerodev.deliverytracker.data.datasource.local

import androidx.paging.PagingSource
import com.zerodev.deliverytracker.data.datasource.db.LogLocationDao
import com.zerodev.deliverytracker.data.model.LogLocation

interface LocalDataSource {
    suspend fun insertLogLocation(logLocation: LogLocation)
    fun getAllLogLocations(): PagingSource<Int, LogLocation>
    suspend fun deleteAllLogLocations()
}

class LocalDataSourceImpl(private val logLocationDao: LogLocationDao) : LocalDataSource {
    override suspend fun insertLogLocation(logLocation: LogLocation) {
        logLocationDao.insertLogLocation(logLocation)
    }

    override fun getAllLogLocations(): PagingSource<Int, LogLocation> {
        return logLocationDao.getAllLogLocations()
    }

    override suspend fun deleteAllLogLocations() {
        logLocationDao.deleteAllLogLocations()
    }
}