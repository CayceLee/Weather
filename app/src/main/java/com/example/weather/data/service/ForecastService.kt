package com.example.weather.data.service

import com.example.weather.data.WeatherForecastMetaDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ForecastService {

    @GET("/gridpoints/{gridId}/{gridX},{gridY}/forecast")
    suspend fun getForecast(
        @Path("gridId") gridId: String,
        @Path("gridX") gridX: Int,
        @Path("gridY") gridY: Int,
    ) : Response<WeatherForecastMetaDataModel>

}