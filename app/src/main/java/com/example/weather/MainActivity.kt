package com.example.weather

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodels.ForecastServiceViewModel
import com.example.weather.viewmodels.WeatherServiceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val weatherServiceVM: WeatherServiceViewModel by viewModels()
    private val forecastServiceVM: ForecastServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        if ((ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            ))
        {
            requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                400
            )
            return
        }

        var lat = 0.0
        var lon = 0.0

        fusedLocationProvider.lastLocation.addOnSuccessListener(this) { location: Location? ->
            if (location != null) {
                weatherServiceVM.retrieveWeather(
                    "${location.latitude},${location.longitude}"
                )
            }

        }

        setContent {
            WeatherTheme {
                    MainScreenUI(
                        modifier = Modifier.fillMaxSize(),
                        lat = lat,
                        lon = lon
                    )
                }

            }
        }

    /*
    Create multiple domain models for properties and forecast
    Parse model into domain model into usable vars
    Repository ("datamodel") > Domain Model > VM > UI
     */

    @Composable
    fun MainScreenUI(modifier: Modifier, lat: Double, lon: Double) {
        val weatherState by weatherServiceVM.stateFlow.collectAsState()
        Log.d("stateForecast: ", weatherState.forecast)

        forecastServiceVM.retrieveForecast(
            weatherState.gridId, weatherState.gridX, weatherState.gridY
        )

        val forecastState by forecastServiceVM.stateFlow.collectAsState()

//        Log.d("Forecast: ", currentWeatherForecast.toString())
        Log.d("stateForecastgridID: ", weatherState.gridId)
        Log.d("stateForecastGridX: ", "${weatherState.gridX}")
        Log.d("stateForecastGridY: ", "${weatherState.gridY}")
        Log.d("periods: ", "${forecastState.period}")

        val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)

        val cityName: String = addresses?.firstOrNull()?.locality ?: ""
        val stateName: String = addresses?.firstOrNull()?.adminArea ?: ""

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black,
                )
                .paint(
                    painterResource(R.drawable.weather),
                    contentScale = ContentScale.FillWidth
                ),
        ) {
            Column {
                Text(
                    text = "WeatherDotGOV",
                    color = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Color.Black
                    } else {
                        Color.White
                    },
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            top = 40.dp,
                            bottom = 40.dp
                        )
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "${weatherState.city} , ${weatherState.state}",
                    color = Color.White
                )
            }


        }
    }
}