package com.msayeh.breeze.data.weather.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AlertWithCity(
    @Embedded val alert: AlertEntity,
    @Relation(parentColumn = "cityId", entityColumn = "id")
    val city: CityEntity,
)
