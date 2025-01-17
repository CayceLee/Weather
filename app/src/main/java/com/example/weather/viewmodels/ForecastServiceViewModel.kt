package com.example.weather.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.repo.WeatherServiceRepository
import com.example.weather.ui.DailyForecastViewState
import com.example.weather.ui.WeeklyForecastViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastServiceViewModel: ViewModel() {
    private val _stateFlow = MutableStateFlow(WeeklyForecastViewState.Default)
    val stateFlow = _stateFlow.asStateFlow()
    private val currentState = _stateFlow.value
    private val _day1StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val day1StateFlow = _day1StateFlow.asStateFlow()
    private val _day2StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val day2StateFlow = _day2StateFlow.asStateFlow()
    private val _day3StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val day3StateFlow = _day3StateFlow.asStateFlow()
    private val _day4StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val day4StateFlow = _day4StateFlow.asStateFlow()
    private val _day5StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val day5StateFlow = _day5StateFlow.asStateFlow()
    private val _day6StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val day6StateFlow = _day6StateFlow.asStateFlow()
    private val _night1StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val night1StateFlow = _night1StateFlow.asStateFlow()
    private val _night2StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val night2StateFlow = _night2StateFlow.asStateFlow()
    private val _night3StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val night3StateFlow = _night3StateFlow.asStateFlow()
    private val _night4StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val night4StateFlow = _night4StateFlow.asStateFlow()
    private val _night5StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val night5StateFlow = _night5StateFlow.asStateFlow()
    private val _night6StateFlow = MutableStateFlow(DailyForecastViewState.Default)
    val night6StateFlow = _night6StateFlow.asStateFlow()

    fun retrieveForecast(gridId: String, gridX: Int, gridY: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            val currentForecast = WeatherServiceRepository().getForecast(gridId, gridX, gridY)
            Log.d("retrieveForecast: ", "$currentForecast")

            val day0 = currentForecast?.properties?.periods?.get(0)
            val night0 = currentForecast?.properties?.periods?.get(1)

            val day1 = currentForecast?.properties?.periods?.get(2)
            val night1 = currentForecast?.properties?.periods?.get(3)
            val day2 = currentForecast?.properties?.periods?.get(4)
            val night2 = currentForecast?.properties?.periods?.get(5)
            val day3 = currentForecast?.properties?.periods?.get(6)
            val night3 = currentForecast?.properties?.periods?.get(7)
            val day4 = currentForecast?.properties?.periods?.get(8)
            val night4 = currentForecast?.properties?.periods?.get(9)
            val day5 = currentForecast?.properties?.periods?.get(10)
            val night5 = currentForecast?.properties?.periods?.get(11)
            val weeklyForecast = listOf(
                day0,
                night0,
                day1,
                night1,
                day2,
                night2,
                day3,
                night3,
                day4,
                night4,
                day5,
                night5,
            )

            _stateFlow.update {
                it.copy(
                    period = weeklyForecast
                )
            }
            if (day0?.icon != null &&
                day0.shortForecast != null &&
                day0.temperature != null &&
                day0.temperatureUnit != null &&
                day0.name != null
                )
            {
                _day1StateFlow.update {
                    it.copy(
                        shortForecast = day0.shortForecast,
                        icon = day0.icon.replace("medium", "large"),
                        tempUnit = day0.temperatureUnit,
                        temperature = day0.temperature,
                        dayOfWeek = day0.name
                    )
                }
            }
            if (night0?.icon != null &&
                night0.shortForecast != null &&
                night0.temperature != null &&
                night0.temperatureUnit != null &&
                night0.name != null
                )
            {
                _night1StateFlow.update {
                    it.copy(
                        shortForecast = night0.shortForecast,
                        icon = night0.icon.replace("medium", "large"),
                        tempUnit = night0.temperatureUnit,
                        temperature = night0.temperature,
                        dayOfWeek = night0.name

                    )
                }
            }
            if (day1?.icon != null &&
                day1.shortForecast != null &&
                day1.temperature != null &&
                day1.temperatureUnit != null &&
                day1.name != null
                )
            {
                _day2StateFlow.update {
                    it.copy(
                        shortForecast = day1.shortForecast,
                        icon = day1.icon.replace("medium", "large"),
                        tempUnit = day1.temperatureUnit,
                        temperature = day1.temperature,
                        dayOfWeek = trimDay(day1.name)
                    )
                }
            }
            if (night1?.icon != null &&
                night1.shortForecast != null &&
                night1.temperature != null &&
                night1.temperatureUnit != null &&
                night1.name != null
                )
            {

                _night2StateFlow.update {
                    it.copy(
                        shortForecast = night1.shortForecast,
                        icon = night1.icon.replace("medium", "large"),
                        tempUnit = night1.temperatureUnit,
                        temperature = night1.temperature,
                        dayOfWeek = night1.name
                    )
                }
            }
            if (day2?.icon != null &&
                day2.shortForecast != null &&
                day2.temperature != null &&
                day2.temperatureUnit != null &&
                day2.name != null
                )
            {
                _day3StateFlow.update {
                    it.copy(
                        shortForecast = day2.shortForecast,
                        icon = day2.icon.replace("medium", "large"),
                        tempUnit = day2.temperatureUnit,
                        temperature = day2.temperature,
                        dayOfWeek = trimDay(day2.name)
                    )
                }
            }
            if (night2?.icon != null &&
                night2.shortForecast != null &&
                night2.temperature != null &&
                night2.temperatureUnit != null &&
                night2.name != null
                )
            {
                _night3StateFlow.update {
                    it.copy(
                        shortForecast = night2.shortForecast,
                        icon = night2.icon.replace("medium", "large"),
                        tempUnit = night2.temperatureUnit,
                        temperature = night2.temperature,
                        dayOfWeek = night2.name
                    )
                }
            }
            if (day3?.icon != null &&
                day3.shortForecast != null &&
                day3.temperature != null &&
                day3.temperatureUnit != null &&
                day3.name != null
                )
            {
                _day4StateFlow.update {
                    it.copy(
                        shortForecast = day3.shortForecast,
                        icon = day3.icon.replace("medium", "large"),
                        tempUnit = day3.temperatureUnit,
                        temperature = day3.temperature,
                        dayOfWeek = trimDay(day3.name)
                    )
                }
            }
            if (night3?.icon != null &&
                night3.shortForecast != null &&
                night3.temperature != null &&
                night3.temperatureUnit != null &&
                night3.name != null
            )
            {
                _night4StateFlow.update {
                    it.copy(
                        shortForecast = night3.shortForecast,
                        icon = night3.icon.replace("medium", "large"),
                        tempUnit = night3.temperatureUnit,
                        temperature = night3.temperature,
                        dayOfWeek = night3.name
                    )
                }
            }
            if (day4?.icon != null &&
                day4.shortForecast != null &&
                day4.temperature != null &&
                day4.temperatureUnit != null &&
                day4.name != null
            )
            {
                _day5StateFlow.update {
                    it.copy(
                        shortForecast = day4.shortForecast,
                        icon = day4.icon.replace("medium", "large"),
                        tempUnit = day4.temperatureUnit,
                        temperature = day4.temperature,
                        dayOfWeek = trimDay(day4.name)
                    )
                }
            }
            if (night4?.icon != null &&
                night4.shortForecast != null &&
                night4.temperature != null &&
                night4.temperatureUnit != null &&
                night4.name != null
            )
            {
                _night5StateFlow.update {
                    it.copy(
                        shortForecast = night4.shortForecast,
                        icon = night4.icon.replace("medium", "large"),
                        tempUnit = night4.temperatureUnit,
                        temperature = night4.temperature,
                        dayOfWeek = night4.name
                    )
                }
            }
            if (day5?.icon != null &&
                day5.shortForecast != null &&
                day5.temperature != null &&
                day5.temperatureUnit != null &&
                day5.name != null
            )
            {
                _day6StateFlow.update {
                    it.copy(
                        shortForecast = day5.shortForecast,
                        icon = day5.icon.replace("medium", "large"),
                        tempUnit = day5.temperatureUnit,
                        temperature = day5.temperature,
                        dayOfWeek = trimDay(day5.name)
                    )
                }
            }
            if (night5?.icon != null &&
                night5.shortForecast != null &&
                night5.temperature != null &&
                night5.temperatureUnit != null &&
                night5.name != null
            )
            {
                _night6StateFlow.update {
                    it.copy(
                        shortForecast = night5.shortForecast,
                        icon = night5.icon.replace("medium", "large"),
                        tempUnit = night5.temperatureUnit,
                        temperature = night5.temperature,
                        dayOfWeek = trimDay(night5.name)
                    )
                }
            }
        }
    }
    private fun trimDay(day: String) :String {
        val daysOfWeek = listOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )
        for (match in daysOfWeek) {
            if (day.contains(match)) {
                return day.substring(0,3)
            }
        }
        return "HOL"
    }
}