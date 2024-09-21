package com.zerodev.deliverytracker.domain.usecases

import com.zerodev.deliverytracker.data.model.LogLocation
import com.zerodev.deliverytracker.domain.repositories.LogLocationRepository

interface InsertLocationUseCase {
    suspend operator fun invoke(logLocation: LogLocation)
}

class InsertLogLocation(private val logLocationRepository: LogLocationRepository) : InsertLocationUseCase {
    override suspend operator fun invoke(logLocation: LogLocation) {
        logLocationRepository.insertLogLocation(logLocation)
    }
}