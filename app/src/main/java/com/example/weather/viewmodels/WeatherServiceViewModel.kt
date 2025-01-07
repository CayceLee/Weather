package com.example.weather.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.repo.WeatherServiceRepository
import com.example.weather.ui.WeeklyWeatherViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherServiceViewModel: ViewModel() {
    private val _stateFlow = MutableStateFlow(WeeklyWeatherViewState.Default)
    val stateFlow = _stateFlow.asStateFlow()
    private val currentState = _stateFlow.value

    fun retrieveWeather(latLongString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentWeather = WeatherServiceRepository().getWeather(latLongString)
            Log.d("CurrentWeather: ", "$currentWeather")
            currentWeather?.properties?.forecast?.let { currentState.copy(forecast = it) }
                ?.let { _stateFlow.emit(it) }
        }
    }



}