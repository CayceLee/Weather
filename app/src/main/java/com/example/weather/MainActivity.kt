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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.weather.ui.theme.WeatherTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var weatherService: WeatherService
    private val apiKey = "MY_WEATHER_API_KEY"


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
                lat = location.latitude
                lon = location.longitude
            }
        weatherService = Retrofit.Builder()
            .baseUrl("https://api.weather.gov/points/$lat,$lon/forecast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        Log.d("Weather: ", "$weatherService")

        setContent {
            WeatherTheme {

//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    mainScreenUI(
                        modifier = Modifier.fillMaxSize(),
                        lat = lat,
                        lon = lon
                    )
//                    DisplayLatLon(
//                        lat = lat,
//                        lon = lon,
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }

            }
//        }
        }
    }

    @Composable
    fun DisplayLatLon(lat: Double?, lon: Double?, modifier: Modifier = Modifier) {
        val url =  "https://api.weather.gov/points/$lat,$lon/forecast"

        Text(
            text = "Latitude: $lat \nLongitude: $lon",
            modifier = modifier
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun mainScreenUI(modifier: Modifier, lat: Double, lon: Double) {

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)

        val cityName: String = addresses?.get(0)?.locality ?: ""
        val stateName: String = addresses?.get(0)?.adminArea ?: ""

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
                    "WeatherDotGov",
                    color = if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
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