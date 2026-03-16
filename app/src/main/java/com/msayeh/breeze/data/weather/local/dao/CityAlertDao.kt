package com.msayeh.breeze.data.weather.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.msayeh.breeze.data.weather.local.entities.AlertEntity
import com.msayeh.breeze.data.weather.local.entities.AlertWithCity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityAlertDao {
    @Transaction
    @Query("SELECT * FROM alerts")
    fun observeAllCitiesWithAlerts(): Flow<List<AlertWithCity>>

    @Transaction
    @Query("SELECT * FROM alerts WHERE isEnabled = 1")
    fun observeActiveAlerts(): Flow<List<AlertWithCity>>

    @Upsert
    suspend fun upsertAlert(alert: AlertEntity)

    @Query("UPDATE alerts SET isEnabled = :enabled WHERE id = :alertId")
    suspend fun updateAlertEnabled(alertId: Int, enabled: Boolean)

    @Query("DELETE FROM alerts WHERE id = :alertId")
    suspend fun deleteAlert(alertId: Int): Int
}