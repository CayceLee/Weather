package com.example.weather.ui

data class DailyForecastViewState(
    val shortForecast: String,
    val icon: String,
    val tempUnit: String,
    val temperature: Int,
    val dayOfWeek: String
) {
    companion object {
        val Default = DailyForecastViewState(
            shortForecast = "",
            icon = "",
            tempUnit = "",
            temperature = 0,
            dayOfWeek = ""
        )
    }
}