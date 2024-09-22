package com.zerodev.deliverytracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.zerodev.deliverytracker.data.model.LogLocation
import com.zerodev.deliverytracker.domain.usecases.GetLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LogLocationViewModel(private val getLogLocationUseCase: GetLocationUseCase) : ViewModel() {
    private val _logLocations = MutableStateFlow<List<LogLocation>>(emptyList())
    val logLocations = _logLocations.asStateFlow()

    fun getAllLogLocations() = getLogLocationUseCase.invoke()

    fun setLogLocations(locations: List<LogLocation>) {
        _logLocations.value = locations
    }
}