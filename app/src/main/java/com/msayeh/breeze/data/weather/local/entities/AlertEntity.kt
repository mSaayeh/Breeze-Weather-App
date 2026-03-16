package com.msayeh.breeze.data.weather.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alerts",
    foreignKeys = [ForeignKey(
        entity = CityEntity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("cityId")],
)
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityId: Int,
    val alertTime: Long,
    val alertType: Int,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
)
