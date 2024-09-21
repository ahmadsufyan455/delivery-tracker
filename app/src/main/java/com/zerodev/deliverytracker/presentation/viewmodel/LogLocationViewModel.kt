package com.zerodev.deliverytracker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zerodev.deliverytracker.data.model.LogLocation
import com.zerodev.deliverytracker.domain.usecases.GetLocationUseCase

class LogLocationViewModel(private val getLogLocationUseCase: GetLocationUseCase) : ViewModel() {
    fun getAllLogLocations(): LiveData<PagingData<LogLocation>> =
        getLogLocationUseCase.invoke().cachedIn(viewModelScope).asLiveData()
}