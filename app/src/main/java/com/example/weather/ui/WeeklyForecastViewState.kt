package com.example.weather.ui

import com.example.weather.data.WeatherForecastMetaDataModel

data class WeeklyForecastViewState(
    val period: List<WeatherForecastMetaDataModel.Properties.Period?>,
) {
    companion object {
        val Default = WeeklyForecastViewState(
            period = emptyList()
        )
    }
}
