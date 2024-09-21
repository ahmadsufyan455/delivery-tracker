package com.zerodev.deliverytracker.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zerodev.deliverytracker.data.datasource.local.LocalDataSource
import com.zerodev.deliverytracker.data.model.LogLocation
import com.zerodev.deliverytracker.domain.repositories.LogLocationRepository
import kotlinx.coroutines.flow.Flow

class LogLocationRepositoryImpl(private val localDataSource: LocalDataSource) :
    LogLocationRepository {
    override suspend fun insertLogLocation(logLocation: LogLocation) {
        localDataSource.insertLogLocation(logLocation)
    }

    override fun getAllLogLocations(): Flow<PagingData<LogLocation>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { localDataSource.getAllLogLocations() }
        ).flow
    }

    override suspend fun deleteAllLogLocations() {
        localDataSource.deleteAllLogLocations()
    }
}