package com.example.weather.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.repo.WeatherServiceRepository
import com.example.weather.ui.WeeklyForecastViewState
import com.example.weather.ui.WeeklyWeatherViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastServiceViewModel: ViewModel() {
    private val _stateFlow = MutableStateFlow(WeeklyForecastViewState.Default)
    val stateFlow = _stateFlow.asStateFlow()
    private val currentState = _stateFlow.value

    fun retrieveForecast(gridId: String, gridX: Int, gridY: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentForecast = WeatherServiceRepository().getForecast(gridId, gridX, gridY)
            Log.d("retrieveForecast: ", "$currentForecast")
            currentForecast?.properties?.periods?.let { currentState.copy(period = it) }
                ?.let { _stateFlow.emit(it) }
        }
    }
}