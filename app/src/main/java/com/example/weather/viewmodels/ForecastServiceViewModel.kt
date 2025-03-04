package com.example.weather.viewmodels

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
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.json.JSONObject
import java.util.concurrent.Executors


class ForecastServiceViewModel:  ViewModel() {
    private val _stateFlow = MutableStateFlow(WeeklyForecastViewState.Default)
    val stateFlow = _stateFlow.asStateFlow()
    private val _weatherStateFlow = MutableStateFlow(WeeklyWeatherViewState.Default)
    val weatherStateFlow = _weatherStateFlow.asStateFlow()

    fun retrieveForecast(latLongString: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val currentWeather = WeatherServiceRepository().getWeather(latLongString)
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
                                    dayOfWeek = first.name!!,
                                    dayOfWeekSubstring = ""
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
                                dayOfWeek = day.name!!,
                                dayOfWeekSubstring = day.name
                            )
                            val nightVS = NightViewState(
                                shortForecast = it[index + 1]?.shortForecast!!,
                                icon = it[index + 1]?.icon!!,
                                tempUnit = it[index + 1]?.temperatureUnit!!,
                                temperature = it[index + 1]?.temperature!!,
                                dayOfWeek = it[index + 1]?.name!!,
                                dayOfWeekSubstring = it[index + 1]?.name!!
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
        setFirstDay(dailyForecastViewStateList)
        }
    }

    private fun setFirstDay(dayListViewState:  List<DailyForecastViewState>) {
        val daysOfWeek = listOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )
        var today = ""
        var tomorrow = ""
        if (dayListViewState[1].dayViewState.dayOfWeek == "Monday") {
            today = "Sunday"
        } else {
            tomorrow = dayListViewState[1].dayViewState.dayOfWeek
            today = daysOfWeek[daysOfWeek.indexOf(tomorrow) -1]
        }
        dayListViewState[0].dayViewState.dayOfWeek = today
        dayListViewState[0].nightViewState.dayOfWeek = today

        dayListViewState.forEach {
            it.nightViewState.dayOfWeek =
                it.nightViewState.dayOfWeek.replace(" Night", "")

            if (daysOfWeek.contains(it.dayViewState.dayOfWeek)) {
                it.dayViewState.dayOfWeekSubstring = it.dayViewState.dayOfWeek.substring(0,3)
                it.nightViewState.dayOfWeekSubstring = it.dayViewState.dayOfWeek.substring(0,3)
            } else {
                it.dayViewState.dayOfWeekSubstring = ""
                it.nightViewState.dayOfWeekSubstring = ""
            }
        }

        if (dayListViewState.isNotEmpty()) {
            _stateFlow.update {
                it.updateFirstState(dayListViewState)
            }
        }
    }

    fun updateCityAndState(lat: Double, lon: Double) {
        getCityStateFromCoordinates(lat, lon) { cityName, stateName ->
            if (cityName != null && stateName != null) {
                _weatherStateFlow.update {
                    it.copy(
                        city = cityName,
                        state = stateName
                    )
                }
                Log.d("Location", "City: $cityName, State: $stateName")
            } else {
                Log.e("Location", "Failed to retrieve city and state")
            }
        }
    }

    fun updateToday(dayForecast: DailyForecastViewState) {
        viewModelScope.launch(Dispatchers.IO){
            _stateFlow.update {
                it.updateState(dayForecast, _stateFlow.value.weeklyForecast)
            }
        }
    }
}

fun  WeeklyForecastViewState.updateFirstState(
    dailyForecastViewStateList: List<DailyForecastViewState>
): WeeklyForecastViewState {
    return this.copy(
        weeklyForecast = dailyForecastViewStateList.dropLast(2),
        todaysIcon = dailyForecastViewStateList.first().dayViewState.icon,
        tonightsIcon = dailyForecastViewStateList.first().nightViewState.icon,
        todaysHigh = dailyForecastViewStateList.first().dayViewState.temperature,
        todaysLow = dailyForecastViewStateList.first().nightViewState.temperature,
        tempUnit = dailyForecastViewStateList.first().dayViewState.tempUnit,
        todaysShortForecast = dailyForecastViewStateList.first().dayViewState.shortForecast,
        tonightsShortForecast = dailyForecastViewStateList.first().nightViewState.shortForecast,
        todaysDayOfWeek = dailyForecastViewStateList.first().nightViewState.dayOfWeek,
        todaysDaySubString = dailyForecastViewStateList.first().nightViewState.dayOfWeekSubstring
    )
}

fun WeeklyForecastViewState.updateState(
    dayForecast: DailyForecastViewState,
    weeklyForecast: List<DailyForecastViewState>
): WeeklyForecastViewState {
    return this.copy(
        weeklyForecast = weeklyForecast,
        todaysIcon = dayForecast.dayViewState.icon,
        tonightsIcon = dayForecast.nightViewState.icon,
        todaysHigh = dayForecast.dayViewState.temperature,
        todaysLow = dayForecast.nightViewState.temperature,
        tempUnit = dayForecast.dayViewState.tempUnit,
        todaysShortForecast = dayForecast.dayViewState.shortForecast,
        tonightsShortForecast = dayForecast.nightViewState.shortForecast,
        todaysDayOfWeek = dayForecast.nightViewState.dayOfWeek,
        todaysDaySubString = dayForecast.nightViewState.dayOfWeekSubstring
    )
}

//fun ForecastServiceViewModel.newFun(s:String ): String {
//
//    stateFlow.value.weeklyForecast[0].nightViewState.dayOfWeek = s
//    return s
//}
//fun ForecastServiceViewModel.newFun1(s:String ) = s
//
//fun ForecastServiceViewModel.newFun2(s:String ) = operation(s)
//
//fun operation(s:String): String {
//    return s.substring(2)
//}

fun getCityStateFromCoordinates(latitude: Double, longitude: Double, callback: (String?, String?) -> Unit) {
    val client = OkHttpClient()
    val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude"

    // Run network request in a background thread
    Executors.newSingleThreadExecutor().execute {
        try {
            val request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Weather") // Required by Nominatim API
                .build()

            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (!response.isSuccessful || responseData.isNullOrEmpty()) {
                callback(null, null)
                return@execute
            }

            val jsonObject = JSONObject(responseData)
            val address = jsonObject.optJSONObject("address")

            val city = address?.optString("city") ?: address?.optString("town") ?: address?.optString("village")
            val state = address?.optString("state")

            callback(city, state)

        } catch (e: IOException) {
            e.printStackTrace()
            callback(null, null)
        }
    }
}