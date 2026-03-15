package com.msayeh.breeze.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val routeName: String = this::class.qualifiedName ?: "") {
    @Serializable
    object Home : Route()
    @Serializable
    object Cities : Route()
    @Serializable
    object Alerts : Route()
    @Serializable
    object Settings : Route()
    @Serializable
    object AddCity : Route()
}