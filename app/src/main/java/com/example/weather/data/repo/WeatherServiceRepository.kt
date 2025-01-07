package com.example.weather.data.repo

import com.example.weather.data.WeatherMetaDataModel
import com.example.weather.data.service.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceRepository {

    private fun getForecastService() =
        Retrofit.Builder()
            .baseUrl("https://api.weather.gov")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

    suspend fun getWeather(latLonPair: String): WeatherMetaDataModel? {
        getForecastService().getWeather(latLonPair).run {
            if (this.isSuccessful) {
                return this.body()
            } else {
                return null
            }
        }
    }

}