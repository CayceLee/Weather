package com.example.weather.data.service

import com.example.weather.data.WeatherMetaDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("/points/{latLonPair}")
    suspend fun getWeather(
        @Path("latLonPair") latLonPair: String
    ) : Response<WeatherMetaDataModel>
}