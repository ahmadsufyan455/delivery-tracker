package com.zerodev.deliverytracker.domain.repositories

import androidx.paging.PagingData
import com.zerodev.deliverytracker.data.model.LogLocation
import kotlinx.coroutines.flow.Flow

interface LogLocationRepository {
    suspend fun insertLogLocation(logLocation: LogLocation)
    fun getAllLogLocations(): Flow<PagingData<LogLocation>>
    suspend fun deleteAllLogLocations()
}