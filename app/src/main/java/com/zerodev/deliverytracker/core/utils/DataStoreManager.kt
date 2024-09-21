package com.zerodev.deliverytracker.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

object DataStoreManager {
    private lateinit var dataStore: DataStore<Preferences>

    fun initialize(context: Context) {
        if (!::dataStore.isInitialized) {
            dataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("location_service_prefs") }
            )
        }
    }

    fun getDataStore(): DataStore<Preferences> {
        if (!::dataStore.isInitialized) {
            throw IllegalStateException("DataStoreManager is not initialized. Call initialize(context) first.")
        }
        return dataStore
    }
}