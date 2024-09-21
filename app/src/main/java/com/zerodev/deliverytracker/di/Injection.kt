package com.zerodev.deliverytracker.di

import androidx.room.Room
import com.zerodev.deliverytracker.data.datasource.db.LogLocationDatabase
import com.zerodev.deliverytracker.data.datasource.local.LocalDataSource
import com.zerodev.deliverytracker.data.datasource.local.LocalDataSourceImpl
import com.zerodev.deliverytracker.data.repositories.LogLocationRepositoryImpl
import com.zerodev.deliverytracker.domain.repositories.LogLocationRepository
import com.zerodev.deliverytracker.domain.usecases.GetLocationUseCase
import com.zerodev.deliverytracker.domain.usecases.GetLogLocation
import com.zerodev.deliverytracker.domain.usecases.InsertLocationUseCase
import com.zerodev.deliverytracker.domain.usecases.InsertLogLocation
import com.zerodev.deliverytracker.presentation.viewmodel.LogLocationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    factory { get<LogLocationDatabase>().logLocationDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            LogLocationDatabase::class.java,
            "log_location_database.db"
        ).fallbackToDestructiveMigrationFrom().build()
    }
}

val repositoryModule = module {
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
    single<LogLocationRepository> { LogLocationRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory<InsertLocationUseCase> { InsertLogLocation(get()) }
    factory<GetLocationUseCase> { GetLogLocation(get()) }
}

val viewmodelModule = module {
    viewModel { LogLocationViewModel(get()) }
}