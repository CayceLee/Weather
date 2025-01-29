package com.example.weather.ui

data class WeeklyWeatherViewState(
    val city: String,
    val state: String
) {
    companion object {
        val Default = WeeklyWeatherViewState(
            city = "",
            state = ""
        )
    }
}