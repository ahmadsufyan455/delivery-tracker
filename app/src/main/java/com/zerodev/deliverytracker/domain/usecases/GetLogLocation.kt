package com.zerodev.deliverytracker.domain.usecases

import androidx.paging.PagingData
import com.zerodev.deliverytracker.data.model.LogLocation
import com.zerodev.deliverytracker.domain.repositories.LogLocationRepository
import kotlinx.coroutines.flow.Flow

interface GetLocationUseCase {
    fun invoke(): Flow<PagingData<LogLocation>>
}

class GetLogLocation(private val logLocationRepository: LogLocationRepository) : GetLocationUseCase {
    override fun invoke(): Flow<PagingData<LogLocation>> {
        return logLocationRepository.getAllLogLocations()
    }
}