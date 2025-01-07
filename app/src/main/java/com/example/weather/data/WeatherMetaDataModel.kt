package com.example.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherMetaDataModel(
    @SerializedName("geometry")
    val geometry: Geometry?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("properties")
    val properties: Properties?,
    @SerializedName("type")
    val type: String?
) {
    data class Geometry(
        @SerializedName("coordinates")
        val coordinates: List<Double?>?,
        @SerializedName("type")
        val type: String?
    )

    data class Properties(
        @SerializedName("county")
        val county: String?,
        @SerializedName("cwa")
        val cwa: String?,
        @SerializedName("fireWeatherZone")
        val fireWeatherZone: String?,
        @SerializedName("forecast")
        val forecast: String?,
        @SerializedName("forecastGridData")
        val forecastGridData: String?,
        @SerializedName("forecastHourly")
        val forecastHourly: String?,
        @SerializedName("forecastOffice")
        val forecastOffice: String?,
        @SerializedName("forecastZone")
        val forecastZone: String?,
        @SerializedName("gridId")
        val gridId: String?,
        @SerializedName("gridX")
        val gridX: Int?,
        @SerializedName("gridY")
        val gridY: Int?,
        @SerializedName("@id")
        val id: String?,
        @SerializedName("observationStations")
        val observationStations: String?,
        @SerializedName("radarStation")
        val radarStation: String?,
        @SerializedName("relativeLocation")
        val relativeLocation: RelativeLocation?,
        @SerializedName("timeZone")
        val timeZone: String?,
        @SerializedName("@type")
        val type: String?
    ) {
        data class RelativeLocation(
            @SerializedName("geometry")
            val geometry: Geometry?,
            @SerializedName("properties")
            val properties: Properties?,
            @SerializedName("type")
            val type: String?
        ) {
            data class Geometry(
                @SerializedName("coordinates")
                val coordinates: List<Double?>?,
                @SerializedName("type")
                val type: String?
            )

            data class Properties(
                @SerializedName("bearing")
                val bearing: Bearing?,
                @SerializedName("city")
                val city: String?,
                @SerializedName("distance")
                val distance: Distance?,
                @SerializedName("state")
                val state: String?
            ) {
                data class Bearing(
                    @SerializedName("unitCode")
                    val unitCode: String?,
                    @SerializedName("value")
                    val value: Int?
                )

                data class Distance(
                    @SerializedName("unitCode")
                    val unitCode: String?,
                    @SerializedName("value")
                    val value: Double?
                )
            }
        }
    }
}