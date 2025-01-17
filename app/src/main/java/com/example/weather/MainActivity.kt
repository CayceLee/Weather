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
import coil.compose.rememberAsyncImagePainter
import com.example.weather.ui.DailyForecastViewState
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.viewmodels.ForecastServiceViewModel
import com.example.weather.viewmodels.WeatherServiceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar


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
                        modifier = Modifier.fillMaxSize()
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
    fun MainScreenUI(modifier: Modifier) {
        val weatherState by weatherServiceVM.stateFlow.collectAsState()
        Log.d("stateForecast: ", weatherState.forecast)

        forecastServiceVM.retrieveForecast(
            weatherState.gridId, weatherState.gridX, weatherState.gridY
        )

        val day1State by forecastServiceVM.day1StateFlow.collectAsState()
        val nightState1 by forecastServiceVM.night1StateFlow.collectAsState()
        val day2State by forecastServiceVM.day2StateFlow.collectAsState()
        val nightState2 by forecastServiceVM.night2StateFlow.collectAsState()
        val day3State by forecastServiceVM.day3StateFlow.collectAsState()
        val nightState3 by forecastServiceVM.night3StateFlow.collectAsState()
        val day4State by forecastServiceVM.day4StateFlow.collectAsState()
        val nightState4 by forecastServiceVM.night4StateFlow.collectAsState()
        val day5State by forecastServiceVM.day5StateFlow.collectAsState()
        val nightState5 by forecastServiceVM.night5StateFlow.collectAsState()

        val day6State by forecastServiceVM.day6StateFlow.collectAsState()
        val nightState6 by forecastServiceVM.night6StateFlow.collectAsState()

        val listOfStates = listOf(
            day2State,
            nightState2,
            day3State,
            nightState3,
            day4State,
            nightState4,
            day5State,
            nightState5,
            day6State,
            nightState6
        )
//        Log.d("stateForecastgridID: ", weatherState.gridId)
//        Log.d("stateForecastGridX: ", "${weatherState.gridX}")
//        Log.d("stateForecastGridY: ", "${weatherState.gridY}")
//        Log.d("periods: ", "${forecastState.period}")
//
//        Log.d("day1state forecast: ","$day1State")
//        Log.d("day2State forecast from list: ", "${listOfStates[0]}")
//        Log.d("day2state NOT from list: ", "$day2State")

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
                        text = "${weatherState.city} , ${weatherState.state}",
                        color = White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.padding(end = 20.dp, top = 40.dp),
                        fontSize = 40.sp,
                        text = "${day1State.temperature}\u00B0/${nightState1.temperature}\u00B0${day1State.tempUnit}",
                        color = White
                    )
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 20.dp),
                    text = if(dayTime()) {
                        day1State.shortForecast
                    } else {
                        nightState1.shortForecast
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
                                day1State.icon
                            } else {
                                nightState1.icon
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(top = 0.dp)

                    )
                }

                FutureForecastCard(listOfStates)
                Spacer(modifier = Modifier.padding(bottom = 50.dp))

            }
        }
    }
//}



@Composable
fun FutureForecastCard(dayStates: List<DailyForecastViewState>) {

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
                Column {
                    Text(
                        text = dayStates[0].dayOfWeek,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        color = White
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = dayStates[0].icon
                            ),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                //complete to switch days
                            }
                        )
                        Text(
                            text = "${dayStates[0].temperature}/${dayStates[1].temperature}",
                            color = White,
                            fontSize = 20.sp,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    blurRadius = 8.5f
                                )
                            )                            )
                    }
                }
                Column {
                    Text(
                        text = dayStates[2].dayOfWeek,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        color = White
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = dayStates[2].icon,
                                ),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                //complete to switch days
                            }
                        )
                        Text(
                            text = "${dayStates[2].temperature}/${dayStates[3].temperature}",
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
                Column {
                    Text(
                        text = dayStates[4].dayOfWeek,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        color = White
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = dayStates[4].icon,

                                ),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                //complete to switch days
                            }
                        )
                        Text(
                            text = "${dayStates[4].temperature}/${dayStates[5].temperature}",
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
                Column {
                    Text(
                        text = dayStates[6].dayOfWeek,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        color = White
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = dayStates[6].icon
                                ),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                //complete to switch days
                            }
                        )
                        Text(
                            text = "${dayStates[6].temperature}/${dayStates[7].temperature}",
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
                Column {
                    Text(
                        text = dayStates[8].dayOfWeek,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        color = White
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = dayStates[8].icon
                                ),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                //complete to switch days
                            }
                        )
                        Text(
                            text = "${dayStates[8].temperature}/${dayStates[9].temperature}",
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
    }
}

fun dayTime() : Boolean {
    val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
    return hour <= 16
}

