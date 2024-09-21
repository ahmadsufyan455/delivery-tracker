package com.zerodev.deliverytracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zerodev.deliverytracker.domain.usecases.GetLocationUseCase

class LogLocationViewModel(private val getLogLocationUseCase: GetLocationUseCase) : ViewModel() {
    fun getAllLogLocations() = getLogLocationUseCase.invoke().cachedIn(viewModelScope)
}