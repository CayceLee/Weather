package com.example.weather

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.weather.ui.DailyForecastViewState
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodels.ForecastServiceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.Calendar

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val forecastServiceVM: ForecastServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                400
            )
            return
        }

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProvider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    fusedLocationProvider.lastLocation.addOnSuccessListener(this) { lastLocation: Location? ->
                        if (lastLocation != null) {
                            forecastServiceVM.retrieveForecast(
                                "${lastLocation.latitude}${lastLocation.longitude}"
                            )
                        }
                    }
                else {
                    forecastServiceVM.retrieveForecast(
                    "${location.latitude},${location.longitude}"
                    )
                    Log.d("Location", "${location.latitude}, ${location.longitude}")
                    val lat = location.latitude
                    val lon = location.longitude
                    forecastServiceVM.updateCityAndState(lat, lon)
                }
            }

        setContent {
            WeatherTheme {
                    MainScreenUI(
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
    }

    @Composable
    fun MainScreenUI(modifier: Modifier) {
        val location by forecastServiceVM.weatherStateFlow.collectAsState()
        val forecast by forecastServiceVM.stateFlow.collectAsStateWithLifecycle()

        val cityAndState = "${location.city}, ${location.state}"
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Row (
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ){
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 50.dp),
                    text = if (location.city.isEmpty()) "" else cityAndState,
                    color = White
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(end = 20.dp, top = 40.dp),
                    fontSize = 40.sp,
                    text = forecast.todaysDayOfWeek,
                    color = White
                )
            }
            Text(
                modifier = Modifier
                    .padding(end = 20.dp, top = 10.dp)
                    .align(Alignment.End),
                fontSize = 40.sp,
                text = if ( dayTime() ||
                    forecast.weeklyForecast[0].nightViewState.dayOfWeek !=
                    forecast.todaysDayOfWeek) {
                    "${forecast.todaysHigh}\u00B0/" +
                            "${forecast.todaysLow}\u00B0" +
                            forecast.tempUnit
                } else {
                    "${forecast.todaysLow}\u00B0" +
                            forecast.tempUnit
                },
                color = White
            )
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp),
                text = if(dayTime()) {
                    forecast.todaysShortForecast
                } else {
                    forecast.tonightsShortForecast
                },
                fontSize = 20.sp,
                color = White
            )
            Box(
                Modifier.weight(1f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = if (dayTime()) {
                            forecast.todaysIcon
                        } else {
                            forecast.tonightsIcon
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp)

                )
            }
            FutureForecastCard(forecast.weeklyForecast)
            Spacer(modifier = Modifier.padding(bottom = 50.dp))
        }
    }

    @Composable
    fun FutureForecastCard(
        forecast: List<DailyForecastViewState>,
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .background(Transparent),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    forecast.forEach {
                        IndividualForecast(it)
                    }

                }
            }
        }
    }

    @Composable
    fun IndividualForecast(
        forecast: DailyForecastViewState
    ) {
        Column {
            Text(
                text = forecast.dayViewState.dayOfWeekSubstring,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                color = White
            )
            Box(
                contentAlignment = Alignment.Center,
            ) {

                Image(
                    painter = rememberAsyncImagePainter(
                        model =
                        if (dayTime()) {
                            forecast.dayViewState.icon
                        } else {
                            forecast.nightViewState.icon
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = {
                            forecastServiceVM.updateToday(forecast)
                        }
                    )
                )
                Text(
                    text = "${forecast.dayViewState.temperature}" +
                            "/${forecast.nightViewState.temperature}",
                    color = White,
                    fontSize = 20.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            blurRadius = 8.5f
                        )
                    )
                )
            }
        }
    }
}

private fun dayTime() : Boolean {
    val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
    return hour <= 16
}

