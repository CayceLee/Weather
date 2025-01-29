package com.example.weather.ui

data class DailyForecastViewState(
    val dayViewState: DayViewState,
    val nightViewState: NightViewState
) {
    companion object {
        val Default = DailyForecastViewState(
            dayViewState = DayViewState.Default,
            nightViewState = NightViewState.Default
        )
    }
}

data class DayViewState(
    val shortForecast: String,
    val icon: String,
    val tempUnit: String,
    val temperature: Int,
    var dayOfWeek: String
) {
    companion object {
        val Default = DayViewState(
            shortForecast = "",
            icon = "",
            tempUnit = "",
            temperature = 0,
            dayOfWeek = ""
        )
    }
}

data class NightViewState(
    val shortForecast: String,
    val icon: String,
    val tempUnit: String,
    val temperature: Int,
    val dayOfWeek: String
) {
    companion object {
        val Default = NightViewState(
            shortForecast = "",
            icon = "",
            tempUnit = "",
            temperature = 0,
            dayOfWeek = ""
        )
    }
}