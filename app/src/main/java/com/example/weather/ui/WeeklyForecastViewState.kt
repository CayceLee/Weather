package com.example.weather.ui

data class WeeklyForecastViewState(
    val weeklyForecast: List<DailyForecastViewState>,
    val todaysIcon: String,
    val tonightsIcon: String,
    val todaysHigh: Int,
    val todaysLow: Int,
    val tempUnit: String,
    val todaysShortForecast: String,
    val tonightsShortForecast: String,
    val todaysDayOfWeek: String
) {
    companion object {
        val Default = WeeklyForecastViewState(
            weeklyForecast = listOf(DailyForecastViewState.Default),
            todaysIcon = "",
            tonightsIcon = "",
            todaysHigh = 0,
            todaysLow = 0,
            tempUnit = "F",
            todaysShortForecast = "",
            tonightsShortForecast = "",
            todaysDayOfWeek = ""
        )
    }
}
