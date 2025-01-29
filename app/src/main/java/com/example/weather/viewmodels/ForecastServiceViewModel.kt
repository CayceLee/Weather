package com.example.weather.viewmodels

import android.text.TextUtils.substring
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.repo.WeatherServiceRepository
import com.example.weather.ui.DailyForecastViewState
import com.example.weather.ui.DayViewState
import com.example.weather.ui.NightViewState
import com.example.weather.ui.WeeklyForecastViewState
import com.example.weather.ui.WeeklyWeatherViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastServiceViewModel: ViewModel() {
    private val _stateFlow = MutableStateFlow(WeeklyForecastViewState.Default)
    val stateFlow = _stateFlow.asStateFlow()
    private val _weatherStateFlow = MutableStateFlow(WeeklyWeatherViewState.Default)
    val weatherStateFlow = _weatherStateFlow.asStateFlow()

    fun retrieveForecast(latLongString: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val currentWeather = WeatherServiceRepository().getWeather(latLongString)
            val location = currentWeather?.first?.properties?.relativeLocation?.properties
            val currentForecast = currentWeather?.second

            var dailyForecastViewStateList = buildList<DailyForecastViewState> {
                var index = 0
                currentForecast?.properties?.periods?.let {

                    while (index <= (it.size-1)) {
                        if (it[index]?.isDaytime == false && index == 0) {
                            it[0]?.let { first ->
                                val nightViewState = NightViewState(
                                    shortForecast = first.shortForecast!!,
                                    icon = first.icon!!,
                                    tempUnit = first.temperatureUnit!!,
                                    temperature = first.temperature!!,
                                    dayOfWeek = first.name!!
                                )
                                add(
                                    DailyForecastViewState(
                                        dayViewState = DayViewState.Default,
                                        nightViewState = nightViewState
                                    )
                                )
                            }
                            index+=1
                    } else {
                        it[index]?.let { day ->
                            val dayVS = DayViewState(
                                shortForecast = day.shortForecast!!,
                                icon = day.icon!!,
                                tempUnit = day.temperatureUnit!!,
                                temperature = day.temperature!!,
                                dayOfWeek = day.name!!
                            )
                            Log.d("dayVS", "$dayVS")
                            val nightVS = NightViewState(
                                shortForecast = it[index + 1]?.shortForecast!!,
                                icon = it[index + 1]?.icon!!,
                                tempUnit = it[index + 1]?.temperatureUnit!!,
                                temperature = it[index + 1]?.temperature!!,
                                dayOfWeek = it[index + 1]?.name!!
                            )
                            add(
                                DailyForecastViewState(
                                    dayViewState = dayVS,
                                    nightViewState = nightVS
                                )
                            )
                            index+=2
                        }
                    }
                }
            }
        }
        Log.d("LIST: ", "${dailyForecastViewStateList}")
        //
        dailyForecastViewStateList.forEach {
            val daysOfWeek = listOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
            )
            if(daysOfWeek.contains(it.dayViewState.dayOfWeek)) {
                it.dayViewState.dayOfWeek = it.dayViewState.dayOfWeek.substring(0, 3)
            } else {
                it.dayViewState.dayOfWeek = ""
            }
        }

            dailyForecastViewStateList.forEach { today ->
                val tomorrow = dailyForecastViewStateList[+1].dayViewState.dayOfWeek
                val listOfWeekdays = listOf(
                    "Sun",
                    "Mon",
                    "Tue",
                    "Wed",
                    "Thu",
                    "Fri",
                    "Sat"
                )
                var previousDay = ""

                // if today is not a day of the week
                if (!listOfWeekdays.contains(today.dayViewState.dayOfWeek)) {
                    //cycle through the list of days
                    listOfWeekdays.forEach { day ->
                        // and for each day, we check what day tomorrow is,
                        // to then set today using previous day

                        //if tomorrow is the first day in the list (Sun), return Sat
                        if (tomorrow == listOfWeekdays[0]) {
                            today.dayViewState.dayOfWeek = "Sat"
                        }
                        // otherwise, if tomorrow is equal to the item in the list,
                        // return the previous day
                        else if (tomorrow == day) {
                            today.dayViewState.dayOfWeek = previousDay
                        }
                        //if no day matched, set this current day to the previous day
                        previousDay = day

                    }

                }
            }

            if (dailyForecastViewStateList.isNotEmpty()) {
                _stateFlow.update {
                    it.copy(
                        weeklyForecast = dailyForecastViewStateList.dropLast(2),
                        todaysIcon = dailyForecastViewStateList[0].dayViewState.icon,
                        tonightsIcon = dailyForecastViewStateList[0].nightViewState.icon,
                        todaysHigh = dailyForecastViewStateList[0].dayViewState.temperature,
                        todaysLow = dailyForecastViewStateList[0].nightViewState.temperature,
                        tempUnit = dailyForecastViewStateList[0].dayViewState.tempUnit,
                        todaysShortForecast = dailyForecastViewStateList[0].dayViewState.shortForecast,
                        tonightsShortForecast = dailyForecastViewStateList[0].nightViewState.shortForecast
                    )

                }
            }

            _weatherStateFlow.update {
                it.copy(
                    city = location?.city ?: "",
                    state = location?.state ?: ""
                )
            }
        }
    }
}