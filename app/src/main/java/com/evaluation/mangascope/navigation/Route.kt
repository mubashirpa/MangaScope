package com.evaluation.mangascope.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object SignIn : Route()

    @Serializable
    data object Home : Route()
}
