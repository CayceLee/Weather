package com.example.weather.viewmodels

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

//    fun retrieveWeather(latLongString: String) {
//
//        viewModelScope.launch(Dispatchers.IO) {
//            val currentWeather = WeatherServiceRepository().getWeather(latLongString)
//            _stateFlow.update {
//                it.copy(
//                    city = currentWeather?.properties?.relativeLocation?.properties?.city ?: "",
//                    state = currentWeather?.properties?.relativeLocation?.properties?.state ?: "")
//            }
//
//        }
//    }
}