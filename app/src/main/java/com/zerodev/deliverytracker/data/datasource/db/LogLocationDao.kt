package com.zerodev.deliverytracker.data.datasource.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zerodev.deliverytracker.data.model.LogLocation

@Dao
interface LogLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogLocation(logLocation: LogLocation)

    @Query("SELECT * FROM LogLocation ORDER BY id DESC")
    fun getAllLogLocations(): PagingSource<Int, LogLocation>

    @Query("DELETE FROM LogLocation")
    suspend fun deleteAllLogLocations()
}