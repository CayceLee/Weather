package com.example.weather.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.repo.WeatherServiceRepository
import com.example.weather.ui.WeeklyWeatherViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherServiceViewModel: ViewModel() {
    private val _stateFlow = MutableStateFlow(WeeklyWeatherViewState.Default)
    val stateFlow = _stateFlow.asStateFlow()

    fun retrieveWeather(latLongString: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val currentWeather = WeatherServiceRepository().getWeather(latLongString)
            Log.d("CurrentWeather: ", "$currentWeather")

            val forecast = currentWeather?.properties?.forecast
            val gridId = currentWeather?.properties?.gridId
            val gridX = currentWeather?.properties?.gridX
            val gridY = currentWeather?.properties?.gridY
            val city = currentWeather?.properties?.relativeLocation?.properties?.city
            val state = currentWeather?.properties?.relativeLocation?.properties?.state

            if (forecast != null
                && gridId != null
                && gridX != null
                && gridY != null
                && city != null
                && state != null
            ) {
                _stateFlow.update {
                    it.copy(
                        forecast = forecast,
                        gridId = gridId,
                        gridX = gridX,
                        gridY = gridY,
                        city = city,
                        state = state
                    )
                }
            }
        }
    }
}