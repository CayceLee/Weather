package com.example.weather.data.models


import com.google.gson.annotations.SerializedName

data class WeatherForecastMetaDataModel(
    @SerializedName("geometry")
    val geometry: Geometry?,
    @SerializedName("properties")
    val properties: Properties?,
    @SerializedName("type")
    val type: String?
) {
    data class Geometry(
        @SerializedName("coordinates")
        val coordinates: List<List<List<Double?>?>?>?,
        @SerializedName("type")
        val type: String?
    )

    data class Properties(
        @SerializedName("elevation")
        val elevation: Elevation?,
        @SerializedName("forecastGenerator")
        val forecastGenerator: String?,
        @SerializedName("generatedAt")
        val generatedAt: String?,
        @SerializedName("periods")
        val periods: List<Period?>?,
        @SerializedName("units")
        val units: String?,
        @SerializedName("updateTime")
        val updateTime: String?,
        @SerializedName("validTimes")
        val validTimes: String?
    ) {
        data class Elevation(
            @SerializedName("unitCode")
            val unitCode: String?,
            @SerializedName("value")
            val value: Double?
        )

        data class Period(
            @SerializedName("detailedForecast")
            val detailedForecast: String?,
            @SerializedName("endTime")
            val endTime: String?,
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("isDaytime")
            val isDaytime: Boolean?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("number")
            val number: Int?,
            @SerializedName("probabilityOfPrecipitation")
            val probabilityOfPrecipitation: ProbabilityOfPrecipitation?,
            @SerializedName("shortForecast")
            val shortForecast: String?,
            @SerializedName("startTime")
            val startTime: String?,
            @SerializedName("temperature")
            val temperature: Int?,
            @SerializedName("temperatureTrend")
            val temperatureTrend: String?,
            @SerializedName("temperatureUnit")
            val temperatureUnit: String?,
            @SerializedName("windDirection")
            val windDirection: String?,
            @SerializedName("windSpeed")
            val windSpeed: String?
        ) {
            data class ProbabilityOfPrecipitation(
                @SerializedName("unitCode")
                val unitCode: String?,
                @SerializedName("value")
                val value: Any?
            )
        }
    }
}