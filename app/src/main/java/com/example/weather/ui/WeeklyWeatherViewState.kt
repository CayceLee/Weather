package com.example.weather.ui

data class WeeklyWeatherViewState(
    val gridId: String,
    val gridX: Int,
    val gridY: Int,
    val forecast: String,
    val city: String,
    val state: String
) {
    companion object {
        val Default = WeeklyWeatherViewState(
            gridId = "",
            gridX = 0,
            gridY = 0,
            forecast = "",
            city = "",
            state = ""
        )
    }
}