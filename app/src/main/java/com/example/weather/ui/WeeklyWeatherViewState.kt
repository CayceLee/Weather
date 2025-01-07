package com.example.weather.ui

import kotlin.io.encoding.Base64

data class WeeklyWeatherViewState (
    val forecast: String
) {
    companion object {
        val Default = WeeklyWeatherViewState(
            forecast = ""
        )
    }
}