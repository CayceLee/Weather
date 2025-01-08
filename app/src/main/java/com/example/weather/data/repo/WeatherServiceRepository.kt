package com.example.weather.data.repo

import com.example.weather.data.WeatherForecastMetaDataModel
import com.example.weather.data.WeatherMetaDataModel
import com.example.weather.data.service.ForecastService
import com.example.weather.data.service.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceRepository {

    private fun getWeatherService() =
        Retrofit.Builder()
            .baseUrl("https://api.weather.gov")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

    private fun getForecastService() =
        Retrofit.Builder()
            .baseUrl("https://api.weather.gov")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ForecastService::class.java)


    suspend fun getWeather(latLonPair: String): WeatherMetaDataModel? {
        getWeatherService().getWeather(latLonPair).run {
            if (this.isSuccessful) {
                return this.body()
            } else {
                return null
            }
        }
    }

    suspend fun getForecast(gridId: String, gridX: Int, gridY: Int) : WeatherForecastMetaDataModel? {
        getForecastService().getForecast(gridId, gridX, gridY).run {
            if (this.isSuccessful) {
                return this.body()
            } else {
                return null
            }
        }
    }

}