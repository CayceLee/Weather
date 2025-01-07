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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.ViewModel
import com.example.weather.data.WeatherMetaDataModel
import com.example.weather.data.repo.WeatherServiceRepository
import com.example.weather.data.service.WeatherService
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodels.WeatherServiceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collect

import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val apiKey = "MY_WEATHER_API_KEY"
    private val weatherServiceVM: WeatherServiceViewModel by viewModels()

    //    @SuppressLint("MissingPermission")
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
                    mainScreenUI(
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
    fun DisplayLatLon(lat: Double?, lon: Double?, modifier: Modifier = Modifier) {
        val url =  "https://api.weather.gov/points/$lat,$lon/forecast"

        Text(
            text = "Latitude: $lat \nLongitude: $lon",
            modifier = modifier
        )
    }

    @Composable
    fun mainScreenUI(modifier: Modifier, lat: Double, lon: Double) {
        val state by weatherServiceVM.stateFlow.collectAsState()

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
                    text = state.forecast,
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
                    text = "$cityName , $stateName",
                    color = Color.White
                )
            }


        }
    }
}